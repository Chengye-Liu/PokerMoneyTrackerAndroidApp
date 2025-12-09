package com.example.myapplication.model

// 統計資料結構（用於主畫面）
data class PokerStats(
    val hands: Int,                   // 總局數
    val duration: String,             // 總時長
    val roi: Int,                     // 投資報酬率
    val hourly: Int,                  // 時薪
    val totalNetProfit: Int,         // 總盈虧（純盈虧值）
    val totalProfitOnly: Int,        // 累積盈利（純正值）
    val totalLossOnly: Int,          // 累積虧損（純正值）
    val totalBuyIn: Int,             // 累積投入
    val averageProfitPerGame: Int,   // 場均盈虧
    val itmRate: String? = null      // 錦標賽用：入圈率（可為 null）
)