package com.programmers.kmooc.utils

import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.programmers.kmooc.network.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("startDate", "endDate")
    fun setDateText(textView: TextView, start: String, end: String) {
        textView.text = "%s ~ %s".format(start, end)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImage(imageView: ImageView, imageUrl: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            ImageLoader.loadImage(imageUrl ?: "") { bitmap ->
                CoroutineScope(Dispatchers.Main).launch {
                    bitmap?.let {
                        imageView.setImageBitmap(bitmap)
                    } ?: kotlin.run {
                        imageView.setImageResource(android.R.drawable.stat_notify_error)
                    }
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("html")
    fun setHtml(webView: WebView, html: String?) {
        webView.loadData(html, "text/html", "UTF-8")
    }
}