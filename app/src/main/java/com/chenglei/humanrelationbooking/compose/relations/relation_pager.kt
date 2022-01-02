package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.vms.RelationIndexVM
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RelationPager(navController: NavController) {
    val vm = viewModel<RelationIndexVM>()
    val pages = vm.pages.collectAsState()
    DisposableEffect(key1 = "") {
        vm.fetchRelationTypes()
        onDispose { }
    }
    val pagerState = rememberPagerState(pageCount = pages.value.size)
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        RelationTabs(pagerState)
        RelationListPagers(navController,pagerState)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun RelationTabs(pagerState: PagerState) {

    val vm = viewModel<RelationIndexVM>()
    val pages = vm.pages.collectAsState()
    val selected = remember {
        mutableStateOf(0)
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collect { index ->
            selected.value = index
        }
    }
    ScrollableTabRow(
        selectedTabIndex = selected.value,
        backgroundColor = Color.White,
        edgePadding = 0.dp,
        indicator = {
            Box(modifier = Modifier.height(0.dp))
        }
    ) {
        pages.value.forEachIndexed { index, title ->
            Tab(
                selected = selected.value == index,
                onClick = {
                    selected.value = index
                    coroutineScope.launch {
                        pagerState.scrollToPage(index)
                    }
                },
                selectedContentColor = colorResource(id = R.color.white),
                text = {
                    if (selected.value == index) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    colorResource(id = R.color.primary_500),
                                    shape = RoundedCornerShape(CornerSize(16.dp))
                                ),
                        ) {
                            Text(
                                text = title, fontSize = 14.sp, fontWeight = FontWeight.W500,
                                color = Color.White,
                                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                            )
                        }
                    } else {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.color_ccccccc)
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RelationListPagers(navController: NavController, pagerState: PagerState) {
    val relationIndexVM = viewModel<RelationIndexVM>()
    val pages = relationIndexVM.pages.collectAsState()
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.white))
    ) {pageIndex->
        // 子页面
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.color_f6f6f6))) {

            val owner = LocalViewModelStoreOwner.provides(relationIndexVM.getPagerViewModelStore(pageIndex))
            CompositionLocalProvider(owner) {
                RelationList(navController, relation = pages.value[pageIndex], position = pageIndex)
            }
        }
    }
}