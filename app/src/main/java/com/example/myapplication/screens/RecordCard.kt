package com.example.myapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.PokerRecordEntity

@Composable
fun RecordCard(record: PokerRecordEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${record.type}｜${record.gameName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (record.profit >= 0) "+${record.profit}" else "${record.profit}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (record.profit >= 0) Color(0xFF00C853) else Color.Red
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text("買入：${record.buyIn}　買出：${record.cashOut}", fontSize = 14.sp)
            Text("時薪：${record.hourly} / 小時　ROI：${record.roi}%", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(4.dp))

            Text(record.date, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
