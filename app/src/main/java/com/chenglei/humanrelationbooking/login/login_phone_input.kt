package com.chenglei.humanrelationbooking.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.ui.CommonInput

@Preview
@Composable
fun LoginPhoneInput() {
    val phoneLoginVM = viewModel(LoginViewModel::class.java)
    val phone = phoneLoginVM.phoneNumber.observeAsState()
    val lineColorRed = remember {
        mutableStateOf(true)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        CommonInput(
            modifier = Modifier.matchParentSize(),
            value = phone.value ?: "",
            onValueChange = { number ->
                phoneLoginVM.inputPhoneNumber(number)
            },
            hint = stringResource(id = R.string.login_input_phonenumber),
            singleLine = true,
            maxLines = 1,
            maxLength = 11,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            focusEvent = { state ->
                lineColorRed.value = state.isFocused
            }
        )

        Box(
            Modifier
                .background(
                    colorResource(
                        if (lineColorRed.value) {
                            R.color.primary_500
                        } else {
                            R.color.color_e1e1e1
                        }
                    )
                )
                .fillMaxWidth()
                .height(0.5.dp)
                .align(Alignment.BottomCenter)
        )
    }

}