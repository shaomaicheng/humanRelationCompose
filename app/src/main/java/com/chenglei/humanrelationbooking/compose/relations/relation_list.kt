package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.ComposeActivity
import com.chenglei.humanrelationbooking.base.EventCenter
import com.chenglei.humanrelationbooking.base.EventCenterKey
import com.chenglei.humanrelationbooking.compose.home.Hosts
import com.chenglei.humanrelationbooking.ui.Line
import com.chenglei.humanrelationbooking.ui.ListWithStatus
import com.chenglei.humanrelationbooking.vms.RelationListViewModel
import kotlinx.coroutines.InternalCoroutinesApi


@Composable
fun RelationList(navController:NavController, relation: String, position: Int) {
    val relationListViewModel = viewModel<RelationListViewModel>()
    val context = LocalContext.current
    val modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(start = 12.dp, end = 12.dp, top = 12.dp)
        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    DisposableEffect(key1 = EventCenterKey.RelationRefresh) {
        EventCenter.observer<Boolean>(EventCenterKey.RelationRefresh, relationListViewModel.viewModelScope) {
            relationListViewModel.refresh()
        }
        onDispose {  }
    }
    ListWithStatus(
        vm = relationListViewModel,
        keyGenerator = {item -> item.objectId},
        listModifier = modifier,
        empty = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 78.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_relation_empty),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier
                    .padding(top = 25.dp)
                    .clickable {
                        ComposeActivity.launch(
                            context = context,
                            Hosts.RelationCreate
                        )
                    }) {
                    Text(
                        text = stringResource(id = R.string.relation_add_tips),
                        color = colorResource(id = R.color.primary_500),
                        fontWeight = FontWeight.W400,
                        fontSize = 14.sp,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
        },
        load = {
            relationListViewModel.load(if (position == 0) "" else relation)
        }
    ) {index, item->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .background(Color.White)
                .clickable {
                    ComposeActivity.launch(
                        context, Hosts.RelationDetail, bundleOf(
                            Ext_Relation to item
                        )
                    )
                }
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            item?.name ?: "",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = colorResource(id = R.color.color_333333)
                        )
                    }

                    if (position == 0) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 4.dp,
                                )
                                .background(
                                    colorResource(id = R.color.color_fff1ef),
                                    shape = RoundedCornerShape(4.dp)
                                ),

                            ) {
                            Text(
                                item.relation,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 2.dp,
                                    bottom = 2.dp
                                ),
                                fontWeight = FontWeight.W400,
                                color = colorResource(id = R.color.primary_500)
                            )
                        }
                    }
                }

                Box(Modifier.padding(end = 12.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = null,
                        Modifier
                            .width(18.dp)
                            .height(18.dp)
                    )
                }
            }

            Line(
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                    )
                    .align(Alignment.BottomCenter)
            )
        }
    }
}