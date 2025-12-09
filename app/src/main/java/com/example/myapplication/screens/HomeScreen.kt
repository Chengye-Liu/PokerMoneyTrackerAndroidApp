package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.RecordsViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Comparator
import java.util.Date

@Composable
fun HomeScreen() {
    val viewModel: RecordsViewModel = viewModel()
    var selectedMode by remember { mutableStateOf("總覽") }

    // 取得所有記錄（Room 的 StateFlow）
    val allRecords by viewModel.records.collectAsState()

    // 統計資料（你的 GameStatsCalculator 裡已內建依 mode 過濾）
    val stats by remember(selectedMode, allRecords) {
        derivedStateOf { viewModel.calculateStats(selectedMode, allRecords) }
    }

    // ========= 重點：依「總覽 / 現金 / 錦標」過濾 + 日期排序 → 準備圖表資料（單筆 profit） =========
    val sdf = remember { SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()) }

    val filtered = remember(selectedMode, allRecords) {
        when (selectedMode) {
            "現金" -> allRecords.filter { it.type == "現金" }
            "錦標" -> allRecords.filter { it.type == "錦標" }
            else -> allRecords
        }
    }

    val sorted = remember(filtered) {
        filtered.sortedWith(
            compareBy(nullsLast(Comparator<Date> { a, b -> a.compareTo(b) })) { rec ->
                try { sdf.parse(rec.date) } catch (_: Exception) { null } // 無日期者排最後
            }
        )
    }

    // 傳「單筆盈虧」給圖表（累積在 ProfitChartHelper 內做）
    val chartData: List<Int> = remember(sorted) { sorted.map { it.profit } }
    // =======================================================================================

    Column(modifier = Modifier.fillMaxSize()) {

        // 模式切換按鈕列
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val modes = listOf("總覽", "現金", "錦標")
            modes.forEach { mode ->
                val isSelected = selectedMode == mode
                Text(
                    text = mode,
                    color = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier
                        .background(
                            color = if (isSelected) Color(0xFF2196F3) else Color.LightGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedMode = mode }
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                )
            }
        }

        // 上方固定：總盈虧（使用 totalNetProfit）
        val net = stats.totalNetProfit
        val netText = when {
            net > 0 -> "+$net"
            net < 0 -> "$net"
            else -> "0"
        }
        TopProfitCard(value = netText)

        Spacer(modifier = Modifier.height(12.dp))

        // 中間滾動：統計卡
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("總遊戲局數", "${stats.hands} 局")
                StatCard("總遊戲時長", stats.duration)
                StatCard("投資報酬率", "${stats.roi} %")
                

                StatCard("總時薪", "${stats.hourly}")

                val profitStr = if (stats.totalProfitOnly > 0) "+${stats.totalProfitOnly}" else stats.totalProfitOnly.toString()
                val lossStr = if (stats.totalLossOnly > 0) "-${stats.totalLossOnly}" else stats.totalLossOnly.toString()

                StatCard("累積盈利", profitStr)
                StatCard("累積虧損", lossStr)
                StatCard("累積投入", "${stats.totalBuyIn}")
                StatCard("場均盈虧", "${stats.averageProfitPerGame}")
            }
        }

        // 下方固定：圖表（會依 selectedMode 顯示對應資料）
        BottomChartCard(chartData = chartData)
    }
}
