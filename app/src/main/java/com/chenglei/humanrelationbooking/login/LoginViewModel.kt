package com.chenglei.humanrelationbooking.login

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import com.chenglei.humanrelationbooking.BuildConfig
import com.chenglei.humanrelationbooking.applog
import com.chenglei.humanrelationbooking.profile.UserBase

class LoginViewModel : ViewModel() {
    private val _uiStatus: MutableLiveData<LoginUIStatus> =
        MutableLiveData(LoginUIStatus.WaitInputPhone)
    val uistatus: LiveData<LoginUIStatus>
        get() = _uiStatus

    val confirmButtonEnable: LiveData<Boolean> = Transformations.map(
        uistatus
    ) {
        confirmButtonEnable()
    }

    private val _phoneNumber: MutableLiveData<String> = MutableLiveData("")
    val phoneNumber: LiveData<String>
        get() = _phoneNumber

    private val _smsCode: MutableLiveData<String> = MutableLiveData("")
    val smsCode: LiveData<String>
        get() = _smsCode

    private val _agree: MutableLiveData<Boolean> = MutableLiveData(false)
    val agree: LiveData<Boolean> = _agree

    private val phoneNumberMatch by lazy {
        PhoneNumberMatch()
    }

    private val smsCodeMatch by lazy {
        SmsCodeMatch()
    }

    fun inputPhoneNumber(phone: String) {
        _phoneNumber.value = phone
        _uiStatus.value = if (phoneNumberMatch.match(phone)) {
            LoginUIStatus.AlreadyInputPhone
        } else {
            LoginUIStatus.WaitInputPhone
        }
    }

    fun inputSmsCode(code: String) {
        _smsCode.value = code
        _uiStatus.value = if (smsCodeMatch.match(code)) {
            LoginUIStatus.AlreadySms
        } else {
            LoginUIStatus.WaitInputSms
        }
    }

    private fun confirmButtonEnable(): Boolean {
        return if (uistatus.value?.lessThen(LoginUIStatus.WaitInputSms) == true) {
            uistatus.value == LoginUIStatus.AlreadyInputPhone
        } else {
            !smsCode.value.isNullOrEmpty() && smsCode.value?.length == 6
        }
    }

    fun smsCodeStep(): Boolean {
        return uistatus.value?.moreThen(LoginUIStatus.AlreadyInputPhone) == true
    }

    fun nextStep() {
        _uiStatus.value = LoginUIStatus.WaitInputSms
    }

    fun backToPhoneStep() {
        _uiStatus.value = LoginUIStatus.AlreadyInputPhone
    }

    fun agreeOrNot() {
        _agree.value = !(agree.value ?: false)
    }


    private val timer = object : Runnable {
        override fun run() {
            _countDown.value = _countDown.value!! - 1
            if (_countDown.value!! > 0) {
                senderHandler.postDelayed(this, 1 * 1000)
            } else {
                _countDown.value = 0
            }
        }
    }


    private val Resend_Timer = 60
    private val _countDown: MutableLiveData<Int> = MutableLiveData(0)
    val countDown: LiveData<Int> = _countDown


    val resendCounting: LiveData<Boolean> = Transformations.map(_countDown) {
        it > 0
    }

    override fun onCleared() {
        super.onCleared()
        senderHandler.removeCallbacks(timer)
    }

    private fun startResendTimer() {
        _countDown.value = Resend_Timer
        senderHandler.post(timer)
    }

    private val senderHandler = android.os.Handler(Looper.getMainLooper())

    fun sendSms(successCallback: (Boolean) -> Unit) {
        if (resendCounting.value == true) return
        startResendTimer()
        BmobSMS.requestSMSCode(_phoneNumber.value,
            "", object : QueryListener<Int>() {
                override fun done(smsId: Int?, e: BmobException?) {
                    successCallback.invoke(e == null)
                }

            })
    }


    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun login(callback: (Boolean, Exception?) -> Unit) {
        val phone = _phoneNumber.value
        val smsCode = _smsCode.value

        val loginCallback = {success:Boolean,e:java.lang.Exception?->
            _loading.value=false
            callback.invoke(success, e)
        }
        _loading.value = true

        if (BuildConfig.DEBUG) {
            applog("debug，走用户名密码注册登录逻辑")
            _loginBmobSDK(phone, smsCode, loginCallback)
            return
        }

        smsCode?.takeIf { it.length == 6 }?.let { smsCode ->
            phone?.takeIf { it.length == 11 }?.let { phone ->
                BmobUser.signOrLoginByMobilePhone(phone, smsCode, object : LogInListener<BmobUser>() {
                    override fun done(user: BmobUser?, e: BmobException?) {
                        e?.apply {
                            printStackTrace()
                            loginCallback.invoke(false, e)
                        }
                        if (e == null) {
                            loginCallback.invoke(true, null)
                        }
                    }
                })
            }
        }
    }

    /**
     * 测试用，节省一下短信费
     * 直接用手机号作为用户名，验证码输入的地方作为密码去注册或者登录
     */
    private fun _loginBmobSDK(
        phone: String?,
        password: String?,
        loginCallback: (Boolean, Exception?) -> Unit
    ) {
        val user = UserBase()
        user.username = phone
        user.setPassword(password)
        BmobQuery<UserBase>().findObjects(
            object : FindListener<UserBase>() {
                override fun done(users: MutableList<UserBase>?, e: BmobException?) {
                    e?.apply {
                        this.printStackTrace()
                        loginCallback.invoke(false, e)
                    }
                    if (e == null) {
                        applog("查找用户。${users?.map { it.objectId }}")
                        users?.let { users ->
                            if (users.isEmpty()) {
                                user.signUp(object : SaveListener<UserBase>() {
                                    override fun done(userBase: UserBase?, e: BmobException?) {
                                        e?.apply {
                                            printStackTrace()
                                            loginCallback.invoke(false,e)
                                        }
                                        if (e == null) {
                                            // 注册成功，登录
                                                applog("注册成功")
                                            _loginBmobSDKByName(user,loginCallback)
                                        }
                                    }
                                })
                            } else {
                                // 直接登录
                                applog("直接登录")
                                _loginBmobSDKByName(user,loginCallback)
                            }
                        }
                    }
                }

            }
        )

    }

    fun _loginBmobSDKByName(
        user:BmobUser,
        loginCallback: (Boolean, Exception?) -> Unit
    ) {
        user.login(object : SaveListener<UserBase>() {
            override fun done(userBase: UserBase?, e: BmobException?) {
                e?.apply {
                    printStackTrace()
                    loginCallback(false, e)
                }
                if (e == null) {
                    applog("登录成功")
                    loginCallback(true, null)
                }
            }

        })
    }
}

enum class LoginUIStatus(val status: Int) : Comparable<LoginUIStatus> {
    WaitInputPhone(1),  // 等待输入手机号
    AlreadyInputPhone(2), // 输入手机号
    WaitInputSms(3), // 等待输入验证码
    AlreadySms(4), // 已经填写验证码
    Logining(5), // 登录中
    LoginSuccess(6), // 登录成功
    LoginFail(7) // 登录失败
}

fun LoginUIStatus.moreThen(status: LoginUIStatus): Boolean {
    return this.status > status.status
}

fun LoginUIStatus.lessThen(status: LoginUIStatus): Boolean {
    return this.status < status.status
}