package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.EventCenter
import com.chenglei.humanrelationbooking.base.EventCenterKey
import com.chenglei.humanrelationbooking.base.arch.use
import com.chenglei.humanrelationbooking.hideKeyboard
import com.chenglei.humanrelationbooking.ui.*
import com.chenglei.humanrelationbooking.utils.toast
import com.chenglei.humanrelationbooking.vms.RelationEditVM
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RelationCreate(navController: NavController) {
    val relationEditVM = viewModel<RelationEditVM>()

    val loading = remember {
        mutableStateOf(false)
    }

    val backer = rememberBacker()

    DisposableEffect("loadListener") {
        relationEditVM.editDs.use(
            success = {p,t->
                toast(R.string.toast_save_success)
                loading.value = false
                EventCenter.post(EventCenterKey.RelationRefresh, true)
                backer.back()

            },
            loading = {
                loading.value = true
            },
            error = {e->
                toast(R.string.toast_save_fail)
                loading.value = false
            }
        )
        onDispose {
        }
    }

    val phone = relationEditVM.phone.collectAsState()
    val name = relationEditVM.name.collectAsState()
    val remark = relationEditVM.remark.collectAsState()
    val relation = relationEditVM.relation.collectAsState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            RelationSelect { item ->
                relationEditVM.relation.value = item
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = true,
    ) {
        Page(
            handleStatusbar = true,
            pageColor = colorResource(id = R.color.white),
            config = TopConfig(
                needToolbar = true,
                title = stringResource(id = R.string.relation_create)
            ),
            lightStatusbar = true
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.color_f6f6f6))
            ) {
                Column {
                    Column(
                        Modifier
                            .padding(top = 16.dp)
                            .background(Color.White)
                            .fillMaxWidth()
                    ) {

                        BookInput(
                            value = name.value,
                            onValueChange = { value ->
                                relationEditVM.name.value = value
                            },
                            labelRes = R.string.relation_name
                        )

                        Arrow(
                            value = if (relation.value.isEmpty()) stringResource(id = R.string.relation_relation) else relation.value,
                            clicker = {
                                context.hideKeyboard()
                                coroutineScope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    }
                                }
                            }
                        )

                        BookInput(
                            value = phone.value,
                            onValueChange = { value ->
                                relationEditVM.phone.value = value
                            },
                            labelRes = R.string.relation_phone
                        )

                        BookInput(
                            value = remark.value,
                            onValueChange = { value ->
                                relationEditVM.remark.value = value
                            },
                            labelRes = R.string.relation_remark
                        )

                    }

                    CommonButton(
                        onClick = {
                            relationEditVM.save()
                        },
                        title = stringResource(id = R.string.save),
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }


                Loading(loading = loading.value, modifier = Modifier.align(alignment = Alignment.Center)
                    .size(50.dp))
            }
        }
    }
}