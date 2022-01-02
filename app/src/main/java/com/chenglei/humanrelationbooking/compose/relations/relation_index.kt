package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.ComposeActivity
import com.chenglei.humanrelationbooking.hideKeyboard
import com.chenglei.humanrelationbooking.ui.*
import com.chenglei.humanrelationbooking.vms.RelationIndexVM
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
fun RelationIndex(nav: NavHostController) {
    val vm = viewModel<RelationIndexVM>()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val newTypeDialogShow = vm.newTypeDialogShow.collectAsState()
    val newRelationDialogShow = vm.newRelationDialogShow.collectAsState()

    Page(handleStatusbar = false, pageColor = Color.White) {
       Column() {
           Box(Modifier.padding(top =68.dp, start = 12.dp,end = 12.dp )) {
               Header()
           }

           Box(Modifier.padding(top = 12.dp )) {
               RelationPager(nav)
           }

           val controller = rememberBottomDialogController(coroutineScope = coroutineScope) {
               vm.newTypeDialogShow.value = false
           }
           if (newTypeDialogShow.value) {
//               BottomDialog(show = newTypeDialogShow.value,
//                    controller = controller,
//                   config = BottomConfig(height = 200.dp)) {
//                   NewTypeDialog(controller)
//               }
               Dialog(onDismissRequest = {
                   vm.newTypeDialogShow.value = false
               }) {
                   Box(modifier = Modifier.wrapContentHeight()
                       .background(Color.White, RoundedCornerShape(8.dp))) {
                       NewTypeDialog(controller)
                   }
               }
           }

           if (newRelationDialogShow.value) {
               Dialog(onDismissRequest = {
                   vm.newRelationDialogShow.value = false
               }) {
                   NewRelationDialog()
               }
           }
       }
    }
}

@Composable
fun NewTypeDialog(controller: BottomDialogController) {
    val vm = viewModel<RelationIndexVM>()
    val context = LocalContext.current
    Column(
        Modifier
//            .fillMaxSize()
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Box(Modifier
            .background(Color.White)
            .clickable {
                controller.hideBottomDialog()
                vm.newRelationDialogShow.value = true
            }
            .height(56.dp)
            .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.new_relation),
                Modifier.align(Alignment.Center),
                fontSize = 14.sp,
                color = colorResource(id = R.color.color_262626),
                fontWeight = FontWeight.W500
            )

            Line(
                Modifier.align(Alignment.BottomCenter)
            )
        }

        Box(Modifier
            .background(Color.White)
            .clickable {
                ComposeActivity.launch(context, "/relationCreate")
                controller.hideBottomDialog()
            }
            .height(56.dp)
            .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.relation_add_tips),
                Modifier.align(Alignment.Center),
                fontSize = 14.sp,
                color = colorResource(id = R.color.color_262626),
                fontWeight = FontWeight.W500)

            Line(
                Modifier.align(Alignment.BottomCenter)
            )
        }

        Box(Modifier
            .background(Color.White)
            .clickable {
                controller.hideBottomDialog()
            }
            .height(56.dp)
            .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.cancel),
                Modifier.align(Alignment.Center),
                fontSize = 14.sp,
                color = colorResource(id = R.color.color_999999),
                fontWeight = FontWeight.W400)
        }
    }
}



@Composable
fun NewRelationDialog() {
    val context = LocalContext.current
    val vm = viewModel<RelationIndexVM>()
    val inputRelation = remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier
        .wrapContentHeight()
        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        .fillMaxWidth()) {
        Column(Modifier.wrapContentHeight()) {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                BookInput(value = inputRelation.value, onValueChange = { value ->
                    inputRelation.value = value
                }, labelRes = R.string.relation)
            }
            CommonButton(onClick = {
                context.hideKeyboard()
                vm.createNewRelation(inputRelation.value)
            }, modifier = Modifier
                .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
                .fillMaxWidth()
                .height(44.dp),
            title = stringResource(id = R.string.save))
        }
    }
}