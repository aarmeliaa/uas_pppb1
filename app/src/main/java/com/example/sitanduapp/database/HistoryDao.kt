package com.example.sitanduapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: HistoryEntity) // Pakai suspend biar async

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getAllHistory(): List<HistoryEntity>
}