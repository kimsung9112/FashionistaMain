package com.study.poly.fashionista.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.study.poly.fashionista.data.dao.MainUrlDao
import com.study.poly.fashionista.data.entity.MainUrlEntity

@Database(entities = [MainUrlEntity::class], version = 1)
abstract class FashionistaDatabase : RoomDatabase() {

    abstract fun repositoryDao() : MainUrlDao
}