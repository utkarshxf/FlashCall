package com.example.myapplication.myapplication.flashcall.bottomnav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor

//@Composable
//fun CustomBottomNavigation(
//    currentScreenId : String,
//    onItemSelected : (Screen) -> Unit,
//    navController: NavController
//) {
//
//    val item = Screen.items.list
//    val navBackStackEntry = navController.currentBackStackEntry
//    val currentDestination = navBackStackEntry?.destination
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Black, RoundedCornerShape(30.dp))
//            .clip(RoundedCornerShape(30.dp))
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Black, RoundedCornerShape(20.dp))
//                .clip(RoundedCornerShape(20.dp))
//                .padding(10.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceAround
//        ) {
//            item.forEach { item ->
//
//                CustomBottomNavigationItem(
//                    item = ,
//                    isSelected = ,
//                    onClick = { /*TODO*/ },
//                    navController = ,
//                    currentDestination =
//                )
//
//            }
//        }
//    }
//}
//
//@Composable
//fun CustomBottomNavigationItem(
//    item : Screen,
//    isSelected : Boolean,
//    onClick:()->Unit,
//    navController: NavController,
//    currentDestination: NavDestination?
//) {
//    val background = if (isSelected) MainColor else Color.Transparent
//    val contentColor = if (isSelected) Color.White else Color.White
//
//    val screenSelected = currentDestination?.hierarchy?.any {
//        it.route == item.id
//    } == true
//
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(20.dp))
//            .background(background)
//            .clickable {
//                onClick
//
//                navController.navigate(item.id) {
//                    popUpTo(navController.graph.findStartDestination().id)
//                    launchSingleTop = true
//                }
//            }
//    ) {
//
//        Row(
//            modifier = Modifier
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//
//            Icon(
//                painter = painterResource(id = if (isSelected) item.focusedIcon else item.unfocusedIcon),
//                contentDescription = null,
//                tint = contentColor
//            )
//
//            AnimatedVisibility(
//                visible = isSelected
//            ) {
//                Text(
//                    text = item.title,
//                    color = contentColor
//                )
//            }
//
//        }
//
//    }
//}


@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        Screen.Home,
        Screen.Wallet,
        Screen.Profile
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }

    }
}

@Composable
fun AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.id } == true

    val background = if (selected) MainColor else Color.Transparent
    val contentColor = if (selected) Color.White else Color.White


    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.id) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = if (selected) screen.focusedIcon else screen.unfocusedIcon),
                contentDescription = "icon",
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    color = contentColor
                )
            }
        }
    }
}