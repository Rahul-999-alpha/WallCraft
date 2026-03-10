package com.rahul.clearwalls.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class PuterAiService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "PuterAiService"
    }

    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    private fun ensureWebView(bridge: JsBridge) {
        if (webView != null) {
            webView?.removeJavascriptInterface("Android")
            webView?.addJavascriptInterface(bridge, "Android")
            return
        }
        Handler(Looper.getMainLooper()).let { handler ->
            // WebView must be created on main thread; ensureWebView is called from main thread via suspendCancellableCoroutine
        }
        webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            addJavascriptInterface(bridge, "Android")
            loadUrl("file:///android_asset/puter_ai.html")
        }
    }

    suspend fun generateImage(prompt: String): ByteArray =
        suspendCancellableCoroutine { cont ->
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                val bridge = object : JsBridge {
                    @JavascriptInterface
                    override fun onImageReady(base64: String) {
                        try {
                            val bytes = Base64.decode(base64, Base64.DEFAULT)
                            Log.d(TAG, "Image received: ${bytes.size} bytes")
                            if (cont.isActive) cont.resume(bytes)
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to decode base64: ${e.message}")
                            if (cont.isActive) cont.resumeWithException(e)
                        }
                    }

                    @JavascriptInterface
                    override fun onError(message: String) {
                        Log.e(TAG, "Puter.js error: $message")
                        if (cont.isActive) cont.resumeWithException(RuntimeException(message))
                    }

                    @JavascriptInterface
                    override fun onStatus(status: String) {
                        Log.d(TAG, "Puter.js status: $status")
                    }
                }

                try {
                    ensureWebView(bridge)
                    // Escape prompt for JS string
                    val escapedPrompt = prompt
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")

                    // Wait a moment for the page to load if first time, then call generateImage
                    val wv = webView!!
                    val runGenerate = Runnable {
                        wv.evaluateJavascript("generateImage(\"$escapedPrompt\")", null)
                    }
                    // If webview hasn't finished loading yet, use webViewClient callback
                    wv.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            mainHandler.post(runGenerate)
                        }
                    }
                    // If page is already loaded (webView reuse), just run it
                    if (wv.progress == 100) {
                        mainHandler.post(runGenerate)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to start generation: ${e.message}")
                    if (cont.isActive) cont.resumeWithException(e)
                }
            }

            cont.invokeOnCancellation {
                Log.d(TAG, "Generation cancelled")
            }
        }

    interface JsBridge {
        @JavascriptInterface fun onImageReady(base64: String)
        @JavascriptInterface fun onError(message: String)
        @JavascriptInterface fun onStatus(status: String)
    }
}
