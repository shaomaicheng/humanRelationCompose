package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import cn.bmob.v3.BmobUser
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.EventCenter
import com.chenglei.humanrelationbooking.base.EventCenterKey
import com.chenglei.humanrelationbooking.books.create.BookItemType
import com.chenglei.humanrelationbooking.books.create.RecordCreateActivity
import com.chenglei.humanrelationbooking.getString
import com.chenglei.humanrelationbooking.meta.BookItem
import com.chenglei.humanrelationbooking.ui.*
import com.chenglei.humanrelationbooking.vms.RelaitonDetailListVM
import com.chenglei.humanrelationbooking.vms.RelationBooksRequest
import com.chenglei.humanrelationbooking.vms.RelationDetailViewModel
import com.chenglei.humanrelationbooking.vms.RelationItem
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
@Composable
@Preview
fun PageTest() {
    RelationDetail(
        relation = RelationItem(
            "chenglei",
            "朋友",
            "17364513280",
            "备注",
            BmobUser.getCurrentUser().objectId
        ), controller = rememberNavController()
    )
}

@Composable
fun RelationDetail(relation: RelationItem?, controller: NavController) {
    val listViewModel = viewModel<RelaitonDetailListVM>()
    val detailVM = viewModel<RelationDetailViewModel>()
    val items = listViewModel.listFlow.collectAsLazyPagingItems()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val relationState = remember {
        mutableStateOf(relation)
    }
    DisposableEffect(key1 = EventCenterKey.Relationitem) {
        EventCenter.observer<RelationItem>(EventCenterKey.Relationitem, scope) {
            it?.let { relationItem->
                if (relationItem.objectId == relationState.value?.objectId) {
                    relationState.value = relationItem
                }
            }
        }
        EventCenter.observer<BookItem>(EventCenterKey.BookItem, scope = scope) {
            it?.let {
                listViewModel.refresh()
            }
        }
        onDispose {  }
    }
    Page(
        handleStatusbar = true, pageColor = colorResource(id = R.color.white),
        lightStatusbar = true,
        config = TopConfig(true, title = relation?.name ?: "")
    ) {

        Column(
            Modifier
                .background(color = colorResource(id = R.color.color_f6f6f6))
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text(
                    text = relation?.name ?: "",
                    fontSize = 24.sp,
                    color = colorResource(id = R.color.color_333333),
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                )

                Box(
                    Modifier.background(
                        color = colorResource(id = R.color.color_fff1ef),
                        shape = RoundedCornerShape(4.dp),
                    )
                ) {
                    Text(
                        text = relation?.relation ?: "",
                        Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.primary_500)
                    )
                }
            }

            // 手机号
            Box(
                Modifier
                    .padding(
                        top = 16.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .height(54.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(
                        getString(R.string.phone),
                        style = androidx.compose.ui.text.TextStyle(
                            color = colorResource(id = R.color.color_666666),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        ),
                        modifier = Modifier.padding(start = 12.dp)
                    )

                    Text(
                        text = relation?.phone ?: "", style = androidx.compose.ui.text.TextStyle(
                            color = colorResource(id = R.color.primary_500),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        ), modifier = Modifier.padding(end = 12.dp)
                    )
                }

            }


            // 总览

            Box(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                    .fillMaxWidth()

            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.color_FC775B),
                                    colorResource(id = R.color.color_F64935)
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        Modifier.padding(
                            start = 20.dp,
                            top = 16.dp,
                            bottom = 20.dp
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.receive_tips),
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = (relationState.value?.income ?: 0).toString(),
                            fontSize = 36.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp, bottom = 5.dp)
                        )
                        Text(
                            text = String.format(
                                stringResource(id = R.string.spend_content),
                                (relationState.value?.spend?:0).toString()
                            ), fontSize = 16.sp, color = Color.White
                        )
                    }


                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .background(
                                color = Color.White, shape = RoundedCornerShape(24.dp)
                            )
                            .wrapContentWidth()
                            .height(34.dp)
                            .clickable(
                                remember { MutableInteractionSource() },
                                indication = null
                            ) {

                                RecordCreateActivity.launchCreate(context, currentRelation = relation)

                            }
                    ) {
                        Text(
                            text = getString(R.string.newone), modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp)
                                .align(
                                    Alignment.Center
                                ), color = colorResource(id = R.color.primary_500), fontSize = 14.sp
                        )
                    }
                }
            }

//            /// 切换类型
            Box(
                Modifier
                    .background(colorResource(id = R.color.color_F0F0F0))
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                    .height(30.dp)
            ) {

                TypeSwitch()

            }

            /// 列表
            Box(
                Modifier
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 12.dp, bottom = 64.dp, top = 12.dp)
            ) {
                Box(
                    Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxSize()
                ) {
                    ListWithStatus(
                        vm = listViewModel,
                        keyGenerator = { item -> item.objectId },
                        empty =  {
                            Box(Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_relation_empty),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        },
                        load = {
                            listViewModel.load(RelationBooksRequest(relation?.name ?: "", detailVM.type.value.index))
                        }) { index, item ->
                        BookItemWidget(index, item, index == 0, index == items.itemCount - 1)
                    }
                }
            }
        }
    }
}


@Composable
fun TypeSwitch() {
    val vm = viewModel<RelationDetailViewModel>()
    val listVM = viewModel<RelaitonDetailListVM>()
    val select = vm.type.collectAsState()
    TabRow(
        selectedTabIndex = select.value.index,
        backgroundColor = colorResource(id = R.color.color_F0F0F0),
        indicator = {
            Empty()
        }
    ) {
        listOf(
            R.string.all,
            R.string.book_type_income,
            R.string.book_type_spend
        ).mapIndexed { index, i ->
            val title = stringResource(id = i)
            Tab(selected = index == select.value.index, onClick = {

            }) {
                if (index == select.value.index) {
                    ButtonNoIndication(
                        modifier = Modifier
                            .height(26.dp)
                            .fillMaxWidth()
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(6.dp)
                            ),
                        onClick = {
                            vm.type.value = RelationDetailViewModel.Type.of(index)
                        }
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                modifier = Modifier.align(Alignment.Center),
                                color = colorResource(id = R.color.color_262626),
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                } else {
                    ButtonNoIndication(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        onClick = {
                            // 切换
                            vm.type.value = RelationDetailViewModel.Type.of(index)
                            listVM.load(
                                RelationBooksRequest(
                                    listVM.p?.param?.name ?: "",
                                    vm.type.value.index
                                )
                            )
                        }) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                color = colorResource(
                                    id = R.color.color_262626
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


const val Ext_Relation = "relation"