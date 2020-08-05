package com.atul.foodzone.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Dishes")
data class DishEntity(
    @PrimaryKey val id : String,
    @ColumnInfo(name = "dish_name") val DishName : String,
    @ColumnInfo(name = "cost_for_one") val costOfDish : String,
    @ColumnInfo(name = "restaurant_id") val res_id : String
)