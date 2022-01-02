package com.chenglei.humanrelationbooking.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenglei.humanrelationbooking.BooksApp
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.applog
import com.hangshun.huadian.android.common.utils.dp2px


@Composable
fun CommonInput(
    modifier: Modifier,
    hint:String = "",
    value:String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    maxLength:Int = Int.MAX_VALUE,
    focusHandler:Boolean=false,
    focusEvent: ((FocusState)->Unit)?= null
) {
    val content = remember {
        mutableStateOf(value)
    }
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val modifier = if (focusHandler) {
                val focusRequest = remember {
                    FocusRequester()
                }
                DisposableEffect(focusRequest) {
                    focusRequest.requestFocus()
                    onDispose {  }
                }
                Modifier
                    .fillMaxWidth()
                    .onFocusEvent { state ->
                        applog(state.toString())
                        focusEvent?.invoke(state)
                    }
                    .focusRequester(focusRequest)
            } else {
                Modifier.fillMaxWidth()
            }
            BasicTextField(
                modifier = modifier,
                value = content.value,
                onValueChange = { inputContent->
                    val realContent = if (inputContent.length > maxLength) {
                        inputContent.substring(0, maxLength)
                    } else {
                        onValueChange(inputContent)
                        inputContent
                    }
                    content.value = realContent
                },
                keyboardOptions = keyboardOptions,
                singleLine = singleLine,
                maxLines = maxLines,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = colorResource(id = R.color.color_333333)
                )
            )
        }

        if (content.value.isEmpty()) {
            Text(
                text = hint,
                modifier = Modifier.align(Alignment.CenterStart),
                style = TextStyle(
                    color = colorResource(id = R.color.color_ccccccc),
                    fontSize = 16.sp
                )
            )
        }
    }
}


@Composable
fun BookInput(
    value:String,
    onValueChange: (String) -> Unit,
    @StringRes labelRes: Int,
    singleLine:Boolean = true
) {
    val borderColor = colorResource(id = R.color.color_ccccccc)
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .drawBehind {
                val strokeWidth = dp2px(BooksApp.getContext(), 0.5f).toFloat()
                val margin = dp2px(BooksApp.getContext(), 12f).toFloat()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = borderColor,
                    Offset(margin, y), Offset(size.width - margin, y), strokeWidth = strokeWidth
                )
            },
        value = value,
        singleLine=true,
        onValueChange = onValueChange,
        label = {
            Text(stringResource(id = labelRes))
        },
        textStyle = TextStyle(
            color = colorResource(id = R.color.color_333333),
            fontSize = 16.sp,
            fontWeight = FontWeight.W400
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.transparent),
            unfocusedBorderColor = colorResource(id = R.color.transparent),
            cursorColor = colorResource(id = R.color.color_ccccccc),
            focusedLabelColor = colorResource(id = R.color.primary_500)
        ),
    )
}