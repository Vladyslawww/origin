package com.holikov.myclient.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

object HTML {
    private const val NEW_LINE_CHAR = "\n"
    private const val HTML_NEW_LINE = "<br>"

    @JvmStatic
    fun fromHtml(html: String): Spanned {
        val htmlWithNewLines = html.replace(NEW_LINE_CHAR, HTML_NEW_LINE)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlWithNewLines, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlWithNewLines)
        }
    }


}
