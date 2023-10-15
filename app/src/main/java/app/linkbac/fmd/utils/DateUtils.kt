package app.linkbac.fmd.utils

import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
fun dateString(date: Date): String {
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return simpleDateFormat.format(date)
}