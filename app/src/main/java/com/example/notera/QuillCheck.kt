package com.example.notera




// QuillEditorScreen.kt
import android.net.Uri
import android.util.Base64
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuillEditorScreen() {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }


    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val base64 = encodeImageToBase64(context, it)
                base64?.let { encoded ->
                    val js = "insertImageFromAndroid('data:image/png;base64,$encoded');"
                    webView?.evaluateJavascript(js, null)
                }
            }
        }
    )

    Scaffold (

//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { imagePickerLauncher.launch("image/*") }
//            ) {
//                Text("+")
//            }
//        }
    ) { innerPadding ->
        AndroidView(
            factory = { context ->
                val bridge = QuillBridge { html ->
                    println("Got HTML from Quill: $html")
                    // TODO: Save or handle the HTML here
                }

                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()

                    // ✅ This line connects JS → Kotlin
                    addJavascriptInterface(bridge, "AndroidInterface")

                    loadUrl("file:///android_asset/quill/quill_editor.html")

                    webView = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = androidx.compose.ui.graphics.Color.Red, RectangleShape)
        )
    }
}

class QuillBridge(val onContent: (String) -> Unit) {
    @JavascriptInterface
    fun onContent(html: String) {
        onContent(html)
    }
}

private fun encodeImageToBase64(context: android.content.Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        if (bytes != null) {
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
