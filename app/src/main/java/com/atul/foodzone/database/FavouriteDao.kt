package com.atul.foodzone.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteDao {

    @Insert
    fun insertRes(favouriteEntity: FavouriteEntity)

    @Delete
    fun deleteRes(favouriteEntity: FavouriteEntity)

    @Query("SELECT * FROM Restaurants WHERE res_id = :ResId")
    fun isPresent(ResId : String):FavouriteEntity

    @Query("SELECT * FROM Restaurants")
    fun getAllRes():List<FavouriteEntity>
}