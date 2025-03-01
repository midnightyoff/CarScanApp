package com.example.carapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars ORDER BY createdAt DESC")
    fun getAll(): Flow<List<CarEntity>>

    @Insert
    suspend fun insert(car: CarEntity)

    @Delete
    suspend fun delete(car: CarEntity)

    @Query("DELETE FROM cars WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getById(id: Int): CarEntity?
}