package com.study.poly.fashionista.data.entity

data class ClothesEntity(
    var Page_ID: String = "", // 파이어 DB 아이디
    var Name: String = "",    // 상품명
    var Shop: String = "",    // 쇼핑몰
    var Info: String = "",    // 상품정보 ex) #쿨톤,#여름,#데일리
    var TitlePath: String = "" // 상품 대표 이미지 url
)