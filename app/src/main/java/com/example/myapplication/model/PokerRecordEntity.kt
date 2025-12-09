package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poker_records")
data class PokerRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val gameName: String,
    val buyIn: Int,
    val cashOut: Int,
    val profit: Int,
    val hourly: Int,
    val roi: Int,
    val date: String,
    val endDate: String?,
    val duration: String?,
    val note: String?
)

