package com.chenglei.humanrelationbooking.compose.relations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.ComposeActivity
import com.chenglei.humanrelationbooking.ui.ButtonNoIndication
import com.chenglei.humanrelationbooking.vms.RelationDetailViewModel
import com.chenglei.humanrelationbooking.vms.RelationIndexVM
import com.google.accompanist.pager.ExperimentalPagerApi

@Preview
@Composable
fun Header() {
    Column {
        Title()
        Box(Modifier.padding(top = 4.dp, bottom = 12.dp)) {
            Count()
        }
        // todo 搜索
//        Search()
    }
}


@Preview
@Composable
fun Title() {
    val vm = viewModel<RelationIndexVM>()
    Row(
        Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        val context = LocalContext.current
        Text(
            text = stringResource(id = R.string.title_relation),
            fontSize = 24.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black,
        )

        ButtonNoIndication(
            onClick = {
                vm.newTypeDialogShow.value = true
            },
            modifier = Modifier.height(40.dp).padding(end = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview
@Composable
fun Count() {
    val relationIndexVM = viewModel<RelationIndexVM>()
    val count = relationIndexVM.count.collectAsState().value

    DisposableEffect(key1 = "count") {
        relationIndexVM.count()
        onDispose {  }
    }

    Text(
        text = String.format(stringResource(id = R.string.relation_count), count.toString()),
        fontSize = 12.sp,
        color = colorResource(id = R.color.color_999999),
        fontWeight = FontWeight.W400,
    )
}

@Preview
@Composable
fun Search() {
    ButtonNoIndication(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                color = colorResource(id = R.color.color_f6f6f6)
            ),
    ) {
        Row(verticalAlignment= Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(
                start = 12.dp
            )) {
            Image(
                painter = painterResource(id = R.drawable.icon_search),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Text(text = stringResource(id = R.string.search), Modifier.padding(start = 8.dp),fontSize = 14.sp, color = colorResource(
                id = R.color.color_ccccccc
            ))
        }
    }
}