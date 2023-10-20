package app.linkbac.fmd.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp
import app.linkbac.fmd.vm.HomeViewModel
import com.himamis.retex.renderer.android.FactoryProviderAndroid
import com.himamis.retex.renderer.android.graphics.ColorA
import com.himamis.retex.renderer.android.graphics.Graphics2DA
import com.himamis.retex.renderer.share.TeXFormula
import com.himamis.retex.renderer.share.platform.FactoryProvider

fun latexToAnnotatedString(context: Context, latex: String, localDensity: Density): Pair<AnnotatedString, MutableMap<String, InlineTextContent>> {
    // split by \( and \), add text as text, render latex and add as image
    var text: MutableList<Pair<HomeViewModel.TextType, String>> = mutableListOf()
    var latexString = latex
    val parts = latex.split("\\(")
    parts.forEachIndexed { index, s ->
        if(index == 0) {
            text.add(Pair(HomeViewModel.TextType.Text, s))
        } else {
            val latexParts = s.split("\\)")
            text.add(Pair(HomeViewModel.TextType.Latex, latexParts[0]))
            if(latexParts.size > 1) {
                text.add(Pair(HomeViewModel.TextType.Text, latexParts[1]))
            }
        }
    }

    var annotatedString = AnnotatedString.Builder()
    var inlineContentMap = mutableMapOf<String, InlineTextContent>()
    text.forEach {
        if (it.first == HomeViewModel.TextType.Text) {
            annotatedString.append(it.second)
        } else {
            // render latex
            val latexBitmap = latexToBitmap(context, it.second)
            val emToPx = with(localDensity) { 30.sp.toPx() }
            val sf = emToPx / latexBitmap.height.toFloat()
            val scaledBitmap = Bitmap.createScaledBitmap(latexBitmap,
                latexBitmap.width.coerceIn(1, (latexBitmap.width * sf).toInt()),
                latexBitmap.height.coerceIn(1, (latexBitmap.height * sf).toInt()),
                true
            )
            annotatedString.appendInlineContent(id = text.indexOf(it).toString())
            val width = with(localDensity) { scaledBitmap.width.toDp().toSp() }
            val height = with(localDensity) { scaledBitmap.height.toDp().toSp() }
            inlineContentMap[text.indexOf(it).toString()] = InlineTextContent(
                Placeholder(
                    width = width,
                    height = height,
                    PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Image(
                    bitmap = scaledBitmap.asImageBitmap(),
                    contentDescription = null
                )
            }
        }
    }
    return Pair(annotatedString.toAnnotatedString(), inlineContentMap)
}

fun latexToBitmap(context: Context, latex: String): Bitmap {
    if (FactoryProvider.getInstance() == null) {
        FactoryProvider.setInstance(FactoryProviderAndroid(context.assets))
    }

    val formula = TeXFormula(latex)
    val builder = formula.TeXIconBuilder()
    val scaleFactor = context.resources.displayMetrics.scaledDensity
    val icon = builder.setSize(20.0 * scaleFactor).setStyle(0).setType(TeXFormula.SERIF).setFGColor(
        ColorA(Color.BLACK)
    ).build()
    val bitmap = Bitmap.createBitmap(icon.iconWidth, icon.iconHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    var mGraphics = Graphics2DA()
    canvas.drawColor(Color.TRANSPARENT)

    // draw latex
    mGraphics.setCanvas(canvas)
    icon.setForeground(ColorA(Color.BLACK))
    icon.paintIcon(null, mGraphics, 0.0, 0.0)

    return bitmap
}