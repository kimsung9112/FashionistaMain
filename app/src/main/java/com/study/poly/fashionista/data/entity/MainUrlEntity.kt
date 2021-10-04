package com.study.poly.fashionista.data.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FashionistaRepository")
data class MainUrlEntity(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var bannerImage: String = "",
    var hoodImage: String = "",
    var outerImage: String = "",
    var pantsImage: String = "",
    var tShirtImage: String = ""
)
