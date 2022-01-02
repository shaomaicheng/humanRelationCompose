package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenglei.humanrelationbooking.meta.BookItem
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.books.create.BookItemType
import com.chenglei.humanrelationbooking.books.create.RecordCreateActivity
import com.chenglei.humanrelationbooking.ui.Line
import okhttp3.internal.wait
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BookItemWidget(index:Int, item : BookItem, top:Boolean, foot:Boolean) {

    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(65.dp)
        .background(
            color = Color.White
        )) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().clickable {
                RecordCreateActivity.launchEdit(context, item)
            }
        ) {
            Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.padding(end = 20.dp, start = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier= Modifier
                        .background(colorResource(id = if (top) R.color.transparent else R.color.color_f14848))
                        .fillMaxHeight()
                        .weight(1f)
                        .width(1.dp))
                    Box(modifier = Modifier
                        .padding(3.dp)
                        .width(9.dp)
                        .height(9.dp)) {
                        Box(modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.color_f14848),
                                shape = CircleShape
                            )
                            .size(9.dp))
                    }
                    Box(modifier= Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(colorResource(id = if (foot) R.color.transparent else R.color.color_f14848))
                        .width(1.dp))
                }

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = item.bookName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color= colorResource(id = R.color.color_666666)
                    )
                    Text(text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(item.timestamp),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.color_999999),
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(top = 4.dp)
                        )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ){
                Text(
                    text =if (item.type == BookItemType.Income.type) item.money.toString() else "-${item.money}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400,
                    color = colorResource(id = if (item.type == BookItemType.Spend.type) R.color.color_00bc4b else R.color.color_f14848)
                )
                Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .width(24.dp))
            }
        }

        Line(Modifier.padding(start = 32.dp).align(Alignment.BottomCenter))
    }
}