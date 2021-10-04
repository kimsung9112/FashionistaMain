package com.study.poly.fashionista.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.study.poly.fashionista.data.entity.MainUrlEntity

@Dao
interface MainUrlDao {

    @Insert
    suspend fun insertAll(clothesList: List<MainUrlEntity>)



}