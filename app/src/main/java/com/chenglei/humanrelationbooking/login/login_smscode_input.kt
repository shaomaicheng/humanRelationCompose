package com.chenglei.humanrelationbooking.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.ui.CommonInput


@Preview
@Composable
fun LoginSmsCodeInput() {
    Box(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        val loginViewModel = viewModel(LoginViewModel::class.java)
        val context = LocalContext.current
        val resendCounting = loginViewModel.resendCounting.observeAsState()
        val timerCountDown = loginViewModel.countDown.observeAsState()
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            CommonInput(
                modifier = Modifier.weight(2f, true),
                value = "",
                onValueChange = { code ->
                    loginViewModel.inputSmsCode(code)
                },
                hint = stringResource(id = R.string.login_input_sms_hint),
                singleLine = true,
                maxLines = 1,
                maxLength = 6,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            var sendSmsCodeTipModifier = Modifier
                .weight(1f, false)
            if (resendCounting.value != true) {
                sendSmsCodeTipModifier = sendSmsCodeTipModifier.clickable {
                    if (resendCounting.value == true) {
                        return@clickable
                    }
                    if (loginViewModel.phoneNumber.value?.isEmpty() == true) {
                        Toast
                            .makeText(
                                context,
                                R.string.login_phonenumber_isempty,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        return@clickable
                    }
                    loginViewModel.sendSms { success ->
                        Toast
                            .makeText(
                                context,
                                if (success) R.string.login_sms_send_success else R.string.login_sms_send_fail,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }

                }
            }
            Text(
                modifier = sendSmsCodeTipModifier,
                text = if (resendCounting.value == true) {
                    String.format(
                        stringResource(id = R.string.login_smscode_count_down),
                        timerCountDown.value.toString()
                    )
                } else stringResource(R.string.login_input_send),
                style = TextStyle(
                    color = colorResource(id = R.color.primary_500),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
            )
        }

        Box(
            Modifier
                .background(colorResource(R.color.color_e1e1e1))
                .fillMaxWidth()
                .height(0.5.dp)
                .align(Alignment.BottomCenter)
        )
    }
}