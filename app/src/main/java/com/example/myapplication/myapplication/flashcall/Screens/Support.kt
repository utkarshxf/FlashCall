package com.example.myapplication.myapplication.flashcall.Screens

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Support(){
    val url = "https://flashcall.me/support"

    Column {
        Spacer(modifier = Modifier.height(40.dp))

        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.databaseEnabled = true

                // Enable WebChromeClient to log JavaScript console messages
                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(message: ConsoleMessage?): Boolean {
                        message?.let {
                            Log.d("WebViewConsole", "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}")
                        }
                        return true
                    }
                }
                loadUrl(url)
            }
        })
    }

}