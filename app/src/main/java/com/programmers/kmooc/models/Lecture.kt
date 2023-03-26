package com.programmers.kmooc.models

data class Lecture(
    val id: String,                 // 아이디
    val number: String,             // 강좌번호
    val name: String,               // 강좌명
    val classfyName: String,        // 강좌분류
    val middleClassfyName: String,  // 강좌분류2
    val imageLarge: String,   // 강좌 이미지 (media>image>large)
    val shortDescription: String,   // 짧은 설명
    val orgName: String,            // 운영기관
    val start: String,                // 운영기간 시작
    val end: String,                  // 운영기간 종료
    val teachers: String,          // 교수진
    val overview: String             // 상제정보(html)
)