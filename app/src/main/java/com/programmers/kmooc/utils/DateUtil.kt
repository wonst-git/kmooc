package com.programmers.kmooc.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun convertDate(dateString: String): String {
        return try {
            val oldFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())

            format.format(oldFormat.parse(dateString) ?: Date())
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}