package com.example.myapplication.myapplication.flashcall.Data

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument


sealed class VideoCallRoute(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {

    val name: String = route.appendArguments(navArguments)

    data object VideoCall : VideoCallRoute(
        route = "video_call",
        navArguments = listOf(
            navArgument("call_id") {
                type = NavType.StringType
                nullable = false
            },
            navArgument("video_call") {
                type = NavType.BoolType
                nullable = false
            }
        )
    ) {
        const val KEY_CALL_ID = "default_e882046c-d80e-4955-95a0-23ced228da93"
        const val KEY_VIDEO_ID = "true"

        fun createRoute(callId: String, videoCall: Boolean) =
            name.replace("{${navArguments[0].name}}", callId)
                .replace("{${navArguments[1].name}}", videoCall.toString())
    }
}



private fun String.appendArguments(navArguments: List<NamedNavArgument>): String {
    val mandatoryArguments = navArguments.filter { it.argument.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter { it.argument.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$mandatoryArguments$optionalArguments"
}
