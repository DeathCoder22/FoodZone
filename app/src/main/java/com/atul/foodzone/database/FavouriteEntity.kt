package com.atul.foodzone.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
data class FavouriteEntity(
    @PrimaryKey val res_id : String,
    @ColumnInfo(name = "res_name") val resName : String,
    @ColumnInfo(name = "res_rating") val resRating : String,
    @ColumnInfo(name = "res_cost") val resCostForOne : String,
    @ColumnInfo(name = "image_url") val ImageUrl : String
)