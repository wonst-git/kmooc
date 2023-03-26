package com.programmers.kmooc.models

data class LectureList(
    val count: Int,
    val numPages: Int,
    val previous: String,
    val next: String,
    var lectures: List<LectureSimple>
)