package com.study.poly.fashionista.data

data class ClothesModel(
    val page_ID: String = "", // 파이어 DB 아이디
    val name: String = "",    // 상품명
    val shop: String = "",    // 쇼핑몰
    val info: String = "",    // 상품정보 ex) #쿨톤,#여름,#데일리
    val titlePath: String = "", // 상품 대표 이미지 url
    val size : ArrayList<String>? = null,
    val ImageUrl: ArrayList<String>? = null
)