package com.example.myapplication.myapplication.flashcall.bottomnav

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.myapplication.flashcall.R

sealed class Screen(
    val id : String,
    val title : String,
    val focusedIcon : Int,
    val unfocusedIcon : Int
)
{

    object Home : Screen(
        "HomeScreen",
        "Home",
        R.drawable.home_focused,
        R.drawable.home_unfocused
    )

    object Wallet : Screen(
        "WalletScreen",
        "Wallet",
        R.drawable.wallet_focused,
        R.drawable.wallet_unfocused
    )

    object Profile : Screen(
        "ProfileScreen",
        "Profile",
        R.drawable.user_focused,
        R.drawable.user_unfocused
    )

    object items{
        val list = listOf(
            Screen.Home,
            Screen.Wallet,
            Screen.Profile
        )
    }

}