package com.chenglei.humanrelationbooking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.applog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class BottomConfig(
    val height : Dp = 300.dp
)

@Composable
fun rememberBottomDialogController(
    coroutineScope: CoroutineScope,
    dismissRequest: () -> Unit
): BottomDialogController = remember {
    BottomDialogController(coroutineScope, dismissRequest)
}


@OptIn(ExperimentalMaterialApi::class)
class BottomDialogController constructor(
    private val coroutineScope: CoroutineScope,
    val dismissRequest: () -> Unit,
) {
    var bottomSheetState: BottomSheetScaffoldState? = null

    fun hideBottomDialog() {
        coroutineScope.launch {
            delay(100)
            dismissRequest.invoke()
        }
        coroutineScope.launch {
            bottomSheetState?.bottomSheetState?.collapse()
        }
    }
}

/**
 * 底部弹出的对话框
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomDialog(
    show : Boolean,
    controller: BottomDialogController,
    config: BottomConfig = BottomConfig(),
    dialogContent: @Composable ()->Unit) {
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState = rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
    controller.bottomSheetState = bottomSheetState

    if (show) {
        Dialog(onDismissRequest = {
            coroutineScope.launch {
                bottomSheetState.bottomSheetState.collapse()
                controller.dismissRequest.invoke()
            }
        }, DialogProperties(
            usePlatformDefaultWidth = false
        )
        ) {
            DisposableEffect(key1 = "") {
                applog("dialog")
                coroutineScope.launch {
                    delay(100)
                    bottomSheetState.bottomSheetState.expand()
                }
                onDispose {
                    applog("dialog dispose")
                    controller.hideBottomDialog()
                }
            }
            BottomSheetScaffold(scaffoldState = bottomSheetState,
                sheetPeekHeight = 0.dp,
                sheetGesturesEnabled=false,
                sheetBackgroundColor = colorResource(id = R.color.transparent),
                backgroundColor = colorResource(id = R.color.transparent),
                sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                sheetContent = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(config.height)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                        )) {
                        dialogContent()
                    }
                }) {

                ButtonNoIndication(onClick = {
                    controller.hideBottomDialog()
                }, modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.transparent))){}
            }
        }
    }
}