package com.chenglei.humanrelationbooking.compose.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.utils.toast

@Composable
fun MinePeek() {
    Box(
        modifier = Modifier
            .padding(
                top = 159.dp, start = 12.dp, end = 12.dp
            )
            .height(188.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {

        Column {
            Text(
                text = stringResource(id = R.string.mine_tools),
                fontSize = 16.sp,
                color = colorResource(id = R.color.color_333333),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, start = 12.dp)
            )

            Box(
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
                    .height(64.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp)
                            .fillMaxHeight()
                            .clickable {
                                //导入
                                toast(R.string.next_version_will_have)
                            }
                            .background(
                                color = colorResource(id = R.color.color_fff1ef),
                                shape = RoundedCornerShape(6.dp)
                            )
                    ) {
                        Box(modifier = Modifier.padding(start = 18.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_import),
                                contentDescription = ""
                            )
                        }

                        Column(Modifier.padding(start = 10.dp)) {
                            Text(
                                text = stringResource(id = R.string.data_import_title),
                                fontWeight = FontWeight.W500,
                                color = colorResource(id = R.color.color_333333),
                                fontSize = 16.sp
                            )

                            Text(
                                text = stringResource(id = R.string.data_import_tips),
                                fontWeight = FontWeight.W400,
                                color = colorResource(id = R.color.color_999999),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                            .clickable {
                                //导出
                                toast(R.string.next_version_will_have)
                            }
                            .fillMaxHeight()
                            .background(
                                color = colorResource(id = R.color.color_fff1ef),
                                shape = RoundedCornerShape(6.dp)
                            )
                    ) {
                        Box(modifier = Modifier.padding(start = 18.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_export),
                                contentDescription = ""
                            )
                        }

                        Column(Modifier.padding(start = 10.dp)) {
                            Text(
                                text = stringResource(id = R.string.data_export_title),
                                fontWeight = FontWeight.W500,
                                color = colorResource(id = R.color.color_333333),
                                fontSize = 16.sp
                            )

                            Text(
                                text = stringResource(id = R.string.data_export_tips),
                                fontWeight = FontWeight.W400,
                                color = colorResource(id = R.color.color_999999),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }


            /// 底部四个

            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp, end = 12.dp, top = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 导入联系人
                Column(
                    Modifier
                        .clickable { toast(R.string.next_version_will_have) }
                        .fillMaxHeight()
                        .weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_relation_manager),
                        contentDescription = "", Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.mine_import_people), fontSize = 12.sp,
                        color = colorResource(
                            id = R.color.color_333333
                        ),
                        modifier = Modifier.padding(6.dp)
                    )
                }

                // 事件提醒
                Column(
                    Modifier
                        .clickable { toast(R.string.next_version_will_have) }
                        .fillMaxHeight()
                        .weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_event_notice),
                        contentDescription = "", Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.mine_event_notification),
                        fontSize = 12.sp,
                        color = colorResource(
                            id = R.color.color_333333
                        ),
                        modifier = Modifier.padding(6.dp)
                    )
                }

                // 消息通知
                Column(
                    Modifier
                        .clickable { toast(R.string.next_version_will_have) }
                        .fillMaxHeight()
                        .weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_notfiication),
                        contentDescription = "", Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.mine_notification), fontSize = 12.sp,
                        color = colorResource(
                            id = R.color.color_333333
                        ),
                        modifier = Modifier.padding(6.dp)
                    )
                }

                /// 预设礼金
                Column(
                    Modifier
                        .clickable { toast(R.string.next_version_will_have) }
                        .fillMaxHeight()
                        .weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_money),
                        contentDescription = "", Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.mine_presetting_money),
                        fontSize = 12.sp,
                        color = colorResource(
                            id = R.color.color_333333
                        ),
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }

    }
}