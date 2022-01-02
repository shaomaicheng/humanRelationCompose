package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.books.create.RelationDataSource
import com.chenglei.humanrelationbooking.utils.getString
import com.chenglei.humanrelationbooking.vms.RelationIndexVM


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RelationSelect(selected:(String) -> Unit) {
    val relationIndexVM = viewModel<RelationIndexVM>()
    val relations = relationIndexVM.pages.collectAsState()
    val selectedRelations = relations.value.filterNot { it == getString(R.string.all) }

    DisposableEffect(key1 = "") {
        relationIndexVM.fetchRelationTypes()
        onDispose {  }
    }

    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White, shape = MaterialTheme.shapes.small)
            .padding(start = 12.dp, end = 12.dp, top = 56.dp)) {
        LazyColumn {
            items(selectedRelations.size) {item->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable {
                            selected.invoke(selectedRelations[item])
                        }) {

                    Text(selectedRelations[item],
                        Modifier
                            .padding(start = 12.dp)
                            .align(Alignment.CenterStart))
                }
            }
        }
    }
}