package com.example.myapplication.util

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlin.math.roundToInt

// 盈虧折線圖
@Composable
fun ProfitLineChart(data: List<Int>) {
    val cumulative = data.runningFold(0) { acc, v -> acc + v }.drop(1)
    val pairs = cumulative.mapIndexed { index, y -> (index + 1).toFloat() to y.toFloat() }
    val entryModel = entryModelOf(*pairs.toTypedArray())

    // Y 軸顯示為百位整數，X 軸顯示整數局數
    val yStep = 100f
    val yFormatter: (Float) -> String = { v -> ((v / yStep).roundToInt() * yStep).toInt().toString() }
    val xFormatter: (Float) -> String = { v -> v.toInt().toString() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("總盈虧", color = Color.Black, fontSize = 12.sp)
                Text("局數", color = Color.Black, fontSize = 12.sp)
            }

            Chart(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                chart = lineChart(),
                model = entryModel,
                startAxis = rememberStartAxis(
                    valueFormatter = { value, _ -> yFormatter(value) }
                ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = { value, _ -> xFormatter(value) }
                )
            )
        }
    }
}
