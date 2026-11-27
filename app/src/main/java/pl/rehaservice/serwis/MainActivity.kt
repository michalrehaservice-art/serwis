package pl.rehaservice.serwis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : Activity() {
    private lateinit var web: WebView
    private var fileCb: ValueCallback<Array<Uri>>? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this)
        setContentView(web)

        val loader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.allowFileAccess = false

        web.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                return loader.shouldInterceptRequest(request.url)
            }
        }
        web.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                callback: ValueCallback<Array<Uri>>,
                params: FileChooserParams
            ): Boolean {
                fileCb?.onReceiveValue(null)
                fileCb = callback
                return try {
                    startActivityForResult(params.createIntent(), 1)
                    true
                } catch (e: Exception) {
                    fileCb = null
                    false
                }
            }
        }
        web.addJavascriptInterface(Bridge(), "Android")
        web.loadUrl("https://appassets.androidplatform.net/assets/index.html")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            fileCb?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
            fileCb = null
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val js = "(function(){var d=document.querySelectorAll('dialog[open]');" +
            "if(d.length){d[d.length-1].close();return true}" +
            "if(typeof stack!=='undefined'&&stack.length){back();return true}return false})()"
        web.evaluateJavascript(js) { r ->
            if (r != "true") super.onBackPressed()
        }
    }

    inner class Bridge {
        // Zapytanie do skryptu Google robi system Android (bez ograniczeń CORS); wynik wraca do strony przez OFFcb(id, błąd, tekst)
        @JavascriptInterface
        fun post(id: String, url: String, body: String) {
            thread {
                var err = 0
                var out = ""
                try {
                    val c = URL(url).openConnection() as HttpURLConnection
                    c.requestMethod = "POST"
                    c.doOutput = true
                    c.connectTimeout = 30000
                    c.readTimeout = 180000
                    c.instanceFollowRedirects = true
                    c.setRequestProperty("Content-Type", "text/plain; charset=utf-8")
                    c.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
                    val code = c.responseCode
                    val stream = if (code < 400) c.inputStream else c.errorStream
                    out = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                    if (code >= 400) {
                        err = 1
                        out = "Serwer odpowiedział błędem $code"
                    }
                } catch (e: Exception) {
                    err = 1
                    out = "Brak połączenia z internetem"
                }
                val js = "OFFcb(" + JSONObject.quote(id) + "," + err + "," + JSONObject.quote(out) + ")"
                runOnUiThread { web.evaluateJavascript(js, null) }
            }
        }
    }
}
