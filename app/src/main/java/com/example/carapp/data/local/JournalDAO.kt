package com.example.carapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_items ORDER BY date DESC")
    fun getAll(): Flow<List<JournalItemEntity>>

    @Insert
    suspend fun insert(item: JournalItemEntity)

    @Query("DELETE FROM journal_items WHERE id = :itemId")
    suspend fun deleteById(itemId: String)

    @Query("SELECT * FROM journal_items WHERE carId = :carId ORDER BY date DESC")
    fun getByCarId(carId: Int): Flow<List<JournalItemEntity>>
}