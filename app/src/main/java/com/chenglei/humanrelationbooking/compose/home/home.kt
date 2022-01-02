package com.chenglei.humanrelationbooking.compose.home

import android.media.effect.Effect
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.applog
import com.chenglei.humanrelationbooking.base.ComposeActivity
import com.chenglei.humanrelationbooking.compose.book.AddBookPage
import com.chenglei.humanrelationbooking.compose.mine.MineIndex
import com.chenglei.humanrelationbooking.compose.relations.*
import com.chenglei.humanrelationbooking.compose.settings.Settings
import com.chenglei.humanrelationbooking.login.LoginPage
import com.chenglei.humanrelationbooking.ui.*
import com.chenglei.humanrelationbooking.vms.RelationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable()
fun Nav(start: String = "/login", argument: Bundle? = null) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = start) {
        composable("/") {
            Home()
        }
        composable("/demo") {
            Demo()
        }

        composable("/login") {
            LoginPage(navHostController = navController)
        }

        composable("/relation") {
            RelationIndex(nav = navController)
        }

        composable("/relationCreate") {
            RelationCreate(navController)
        }

        composable(Hosts.RelationDetail, arguments = listOf(
            navArgument(Ext_Relation) {
                defaultValue =
                    argument?.getSerializable(Ext_Relation) ?: RelationItem("", "", "", "", "")
                type = NavType.SerializableType(RelationItem::class.java)
            }
        )) {
            val relation = it.arguments?.getSerializable(Ext_Relation) as? RelationItem
            RelationDetail(relation, navController)
        }

        composable(Hosts.Mine) {
            MineIndex()
        }

        composable(Hosts.Settings) {
            Settings(navController)
        }

        composable(Hosts.AddBook) {
            AddBookPage(navController)
        }
    }
}

interface Hosts {
    companion object {
        const val RelationCreate: String = "/relationCreate"
        const val RelationDetail = "/relationDetail"
        const val Mine = "/mine"
        const val Settings = "/settings"
        const val AddBook = "/addbook"
    }
}

@Composable
@Preview
fun Demo() {
    Page(handleStatusbar = true, pageColor = Color.Black) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
        ) {
        }
    }
}

@Composable
@Preview
fun Home() {
    val openDialog = remember {
        mutableStateOf(false)
    }
    val coroutine = rememberCoroutineScope()
    val controller = rememberBottomDialogController(coroutineScope = coroutine) {
        openDialog.value = false
    }
    Page(
        handleStatusbar = true, pageColor = Color.White, lightStatusbar = true,
        config = TopConfig(true, "")
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {
            Button(onClick = {
                openDialog.value = true
            }, Modifier.background(Color.White)) {
                Text(text = "demo")
            }
            BottomDialog(show = openDialog.value, controller = controller) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.White
                        )
                )
            }
        }
    }
}