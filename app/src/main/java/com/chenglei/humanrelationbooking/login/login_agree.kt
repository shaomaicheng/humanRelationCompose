package com.chenglei.humanrelationbooking.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.applog
import com.chenglei.humanrelationbooking.h5.H5Path
import com.chenglei.humanrelationbooking.h5.WebViewActivity
import com.chenglei.humanrelationbooking.ui.ButtonNoIndication

@Preview
@Composable
fun LoginAgree() {
    val loginVM = viewModel(LoginViewModel::class.java)
    val agree = loginVM.agree.observeAsState()
    val context = LocalContext.current
    Row(
        Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonNoIndication(
            modifier = Modifier.padding(start = 20.dp),
            onClick = {
                // 点击同意
                loginVM.agreeOrNot()
            },
        ) {
            Box(
                Modifier
                    .size(15.dp, 15.dp)
                    .align(Alignment.CenterVertically)
                    .background(
                        shape = CircleShape,
                        color = if (agree.value == true) colorResource(id = R.color.primary_500) else Color.White
                    )
                    .border(
                        width = 1.dp,
                        shape = CircleShape,
                        color = colorResource(
                            id = if (agree.value == true) R.color.primary_500 else R.color.color_ccccccc
                        )
                    )
            )
        }


        val content = buildAnnotatedString {
            withStyle(style = SpanStyle(
                color = colorResource(id = R.color.color_999999),
                fontSize = 12.sp,
                fontWeight = FontWeight.W400
            )) {
                append("登录即代表您已同意")
            }

            pushStringAnnotation(tag="url",annotation = "https://www.baidu.com")

            withStyle(style = SpanStyle(
                color = colorResource(id = R.color.color_999999),
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                textDecoration = TextDecoration.Underline,
            )) {
                append("份子记账用户注册协议")
            }
        }
        ClickableText(
            modifier=Modifier.padding(start = 4.dp),
            text = content,
            onClick = {
                applog(it.toString())
                content.getStringAnnotations(tag = "url", start = it, end = it)
                    .firstOrNull()?.let {annotation ->
                        applog(annotation.item)
                        WebViewActivity.launch(context, H5Path.Login_Proto)
                    }
            }
        )
    }
}