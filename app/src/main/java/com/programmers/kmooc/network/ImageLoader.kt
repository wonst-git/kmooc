package com.programmers.kmooc.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.coroutineScope
import java.net.URL

object ImageLoader {

    private val cacheImages = mutableMapOf<String, Bitmap>()

    suspend fun loadImage(url: String, completed: (Bitmap?) -> Unit) {
        coroutineScope {
            try {
                completed(cacheImages.getOrElse(url) {
                    val bitmap = BitmapFactory.decodeStream(URL(url).openStream())

                    cacheImages[url] = bitmap

                    bitmap
                })
            } catch (e: Exception) {
                completed(null)
            }
        }
    }
}