package com.example.myapplication.util

import com.example.myapplication.model.PokerRecordEntity
import com.example.myapplication.model.PokerStats
import kotlin.math.roundToInt

class GameStatsCalculator {
    fun calculateStats(records: List<PokerRecordEntity>, type: String?): PokerStats {
        val filtered = if (type == null || type == "總覽") records else records.filter { it.type == type }

        var totalMinutes = 0
        var totalNetProfit = 0
        var totalBuyIn = 0

        filtered.forEach { record ->
            val dur = record.duration?.let {
                val parts = it.replace("小時", ":").replace("分", "").split(":")
                val hours = parts.getOrNull(0)?.toIntOrNull() ?: 0
                val mins = parts.getOrNull(1)?.toIntOrNull() ?: 0
                hours * 60 + mins
            } ?: 0
            totalMinutes += dur
            totalNetProfit += record.profit
            totalBuyIn += record.buyIn
        }

        val hands = filtered.size
        val totalLossOnly = filtered.filter { it.profit < 0 }.sumOf { -it.profit }
        val totalProfitOnly = filtered.filter { it.profit > 0 }.sumOf { it.profit }

        val hours = totalMinutes / 60
        val mins = totalMinutes % 60
        val durationStr = "${hours}小時${mins}分"

        val roi = if (totalBuyIn > 0) ((totalNetProfit.toFloat() / totalBuyIn) * 100).toInt() else 0
        val hourly = if (totalMinutes > 0) (totalNetProfit / (totalMinutes / 60f)).roundToInt() else 0
        val avgProfit = if (hands > 0) totalNetProfit / hands else 0

        return PokerStats(
            hands = hands,
            duration = durationStr,
            roi = roi,
            hourly = hourly,
            totalNetProfit = totalNetProfit,
            totalProfitOnly = totalProfitOnly,
            totalLossOnly = totalLossOnly,
            totalBuyIn = totalBuyIn,
            averageProfitPerGame = avgProfit
        )
    }
}
