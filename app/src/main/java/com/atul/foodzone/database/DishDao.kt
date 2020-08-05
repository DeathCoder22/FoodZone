package com.atul.foodzone.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DishDao {

    @Insert
    fun insertDish(dishEntity: DishEntity)

    @Delete
    fun deleteDish(dishEntity: DishEntity)

    @Query("SELECT * FROM Dishes")
    fun getAllDishes():List<DishEntity>

    @Query("SELECT * FROM Dishes WHERE id = :dishId")
    fun getDishByid(dishId : String):DishEntity

    @Query("DELETE FROM Dishes")
    fun deleteAllContents()

}