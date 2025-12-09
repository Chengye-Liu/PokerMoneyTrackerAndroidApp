package com.example.myapplication.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokerRecordDao {

    @Query("SELECT * FROM poker_records ORDER BY id DESC")
    fun getAllRecords(): Flow<List<PokerRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: PokerRecordEntity)

    @Delete
    suspend fun deleteRecord(record: PokerRecordEntity)

    @Query("DELETE FROM poker_records")
    suspend fun clearAll()
}