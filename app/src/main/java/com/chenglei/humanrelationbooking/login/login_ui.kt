package com.chenglei.humanrelationbooking.login

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.home.HomeActivity
import com.chenglei.humanrelationbooking.ui.CommonButton
import com.chenglei.humanrelationbooking.ui.Loading
import com.chenglei.humanrelationbooking.ui.Page


@Composable
fun LoginPage(navHostController: NavHostController) {
    LoginPageInner()
}

@Preview
@Composable
fun LoginPageInner() {
    val loginViewModel = viewModel(LoginViewModel::class.java)
    val loginUIStatus = loginViewModel.uistatus.observeAsState()
    val context = LocalContext.current
    val loadingShow = loginViewModel.loading.observeAsState()

    Page(handleStatusbar = true, pageColor = Color.White, lightStatusbar = true,
        onBack = {
            if (loginUIStatus.value?.moreThen(LoginUIStatus.AlreadyInputPhone) == true) {
                loginViewModel.backToPhoneStep()
                false
            } else {
                true
            }
        }) {
        Box(
            Modifier.fillMaxHeight(),
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, end = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.login_close),
                        contentDescription = null,
                        Modifier
                            .size(width = 24.dp, height = 24.dp)
                            .clickable { }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.login_app_logo),
                        contentDescription = null,
                        Modifier.size(24.dp, 24.dp),
                    )

                    Text(
                        text = stringResource(id = R.string.login_app_name),
                        style = TextStyle(
                            color = colorResource(R.color.primary_500),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W600
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Box(
                    Modifier.padding(start = 24.dp, top = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.login_welcome),
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.W600,
                            color = colorResource(id = R.color.color_333333)
                        )
                    )
                }


                Box(
                    Modifier
                        .padding(bottom = 36.dp, top = 36.dp, start = 4.dp)
                        .wrapContentWidth()
                ) {
                    LoginAgree()
                }


                Box(
                    Modifier
                        .padding(start = 23.dp, end = 24.dp, top = 24.dp)
                        .fillMaxWidth()
                        .height(80.dp)
                ) {

                    if (loginUIStatus.value?.moreThen(LoginUIStatus.AlreadyInputPhone) == true) {
//                    if (loginViewModel.smsCodeStep()) {
                        // 输入验证码
                        LoginSmsCodeInput()
                    } else {
                        // 输入手机号
                        LoginPhoneInput()
                    }
                }

                // 下一步
                Box(Modifier.padding(start = 24.dp, end = 24.dp, top = 36.dp)) {
                    CommonButton(
                        onClick = {
                            when (loginUIStatus.value) {
                                LoginUIStatus.AlreadyInputPhone -> {
                                    loginViewModel.nextStep()
                                }
                                LoginUIStatus.AlreadySms -> {
                                    if (loginViewModel.agree.value != true) {
                                        Toast.makeText(
                                            context,
                                            R.string.login_need_agree,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@CommonButton
                                    }
                                    loginViewModel.login { success, e ->
                                        if (!success) {
                                            Toast.makeText(
                                                context,
                                                String.format(
                                                    context.getString(R.string.login_fail),
                                                    0
                                                ),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                R.string.login_success,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            (context as? Activity)?.apply {
                                                startActivity(Intent(this,HomeActivity::class.java))
                                                this.finish()
                                            }
                                        }
                                    }
                                }
                                else -> {
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        enabled = loginViewModel.confirmButtonEnable.observeAsState().value
                            ?: false,
                        title = stringResource(
                            if (loginViewModel.smsCodeStep()) {
                                R.string.login_confirm
                            } else {
                                R.string.login_next_step
                            }
                        )
                    )
                }
            }


            Loading(loading = loadingShow.value ?: false,
                Modifier.align(Alignment.Center))

        }
    }
}