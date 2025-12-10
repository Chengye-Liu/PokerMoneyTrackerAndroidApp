package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.AppDatabase
import com.example.myapplication.model.PokerRecordEntity
import com.example.myapplication.repository.RecordsRepository
import com.example.myapplication.util.GameStatsCalculator
import com.example.myapplication.model.PokerStats
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecordsRepository
    val records: StateFlow<List<PokerRecordEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).pokerRecordDao()
        repository = RecordsRepository(dao)

        records = repository.allRecords
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addRecord(record: PokerRecordEntity) {
        viewModelScope.launch {
            repository.insert(record)
        }
    }

    fun calculateStats(mode: String, recordList: List<PokerRecordEntity>): PokerStats {
        val type = when (mode) {
            "現金" -> "現金"
            "錦標" -> "錦標"
            else -> null
        }
        return GameStatsCalculator().calculateStats(recordList, type)
    }

    fun deleteRecord(record: PokerRecordEntity) {
        viewModelScope.launch {
            repository.delete(record)
        }
    }
}
