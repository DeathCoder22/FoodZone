package com.atul.foodzone.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FavouriteEntity::class],version = 1)
abstract class FavouriteDatabase: RoomDatabase() {

    abstract fun favouriteDao():FavouriteDao
}