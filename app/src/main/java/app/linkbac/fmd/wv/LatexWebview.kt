package app.linkbac.fmd.wv

import android.content.Context
import android.view.View
import android.webkit.WebView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun LatexWebView(context: Context, question: String) =
    WebView(context).apply {
        settings.javaScriptEnabled = true
        setBackgroundColor(Color.Transparent.toArgb())
        setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        setOnLongClickListener(
            View.OnLongClickListener {
                true
            }
        )
        isLongClickable = false
        isHapticFeedbackEnabled = false
        loadDataWithBaseURL(
            "file:///android_asset/es5/",
            "<head>" +
                    "<script type=\"text/javascript\" id=\"MathJax-script\" async src=\"tex-mml-chtml.js\">\n" +
                    "</script>" +
                    "</head>" +
                    "<body style=\"margin: 0; padding: 0;\">" +
                    question.replace("\n", "<br>") +
                    "</body>",
            "text/html",
            "utf-8",
            null
        )
    }