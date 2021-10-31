package com.study.poly.fashionista.data

data class ClothesModel(
    val Page_ID: String = "", // 파이어 DB 아이디
    val Name: String = "",    // 상품명
    val Shop: String = "",    // 쇼핑몰
    val Info: String = "",    // 상품정보 ex) #쿨톤,#여름,#데일리
    val TitlePath: String = "", // 상품 대표 이미지 url
    val Size : ArrayList<String>? = null,
    val ImageUrl: ArrayList<String>? = null
)