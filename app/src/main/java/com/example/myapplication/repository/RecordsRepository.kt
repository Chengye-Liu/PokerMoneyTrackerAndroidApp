package com.example.myapplication.repository

import com.example.myapplication.model.PokerRecordDao
import com.example.myapplication.model.PokerRecordEntity
import kotlinx.coroutines.flow.Flow

class RecordsRepository(private val dao: PokerRecordDao) {

    val allRecords: Flow<List<PokerRecordEntity>> = dao.getAllRecords()

    suspend fun insert(record: PokerRecordEntity) {
        dao.insertRecord(record)
    }

    suspend fun delete(record: PokerRecordEntity) {
        dao.deleteRecord(record)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
