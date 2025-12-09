package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.model.PokerRecordEntity
import com.example.myapplication.viewmodel.RecordsViewModel

@Composable
fun RecordsScreen(navController: NavController, viewModel: RecordsViewModel = viewModel()) {
    val records by viewModel.records.collectAsState()
    // 儲存被選中的紀錄，用於顯示詳細資訊
    var selectedRecord by remember { mutableStateOf<PokerRecordEntity?>(null) }
    // 控制刪除確認對話框的顯示
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("紀錄", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { navController.navigate("add_record") }) {
                Icon(Icons.Default.Add, contentDescription = "新增紀錄")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(records, key = { it.id }) { record ->
                RecordCard(record = record, onClick = { selectedRecord = record })
            }
        }
    }

    // 記錄點開後的詳細數據框
    selectedRecord?.let { record ->
        Dialog(onDismissRequest = { selectedRecord = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .wrapContentHeight()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "盈虧：${if (record.profit >= 0) "+" else ""}${record.profit}",
                            fontSize = 20.sp,
                            color = if (record.profit >= 0) Color(0xFF2E7D32) else Color.Red,
                            fontWeight = FontWeight.Bold
                        )

                        // 遊戲類型
                        Row {
                            Text("遊戲類型：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = record.type,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // 遊戲名稱
                        Row {
                            Text("遊戲名稱：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = record.gameName,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // 買入
                        Row {
                            Text("買入：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = record.buyIn.toString(),
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // 買出
                        Row {
                            Text("買出：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = record.cashOut.toString(),
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // 開始時間
                        Row {
                            Text("開始時間：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = record.date,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }


                        // 結束時間
                        Row {
                            Text("結束時間：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = record.endDate ?: "未填寫",
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // 時薪
                        Row {
                            Text("時薪：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "+${record.hourly} / 小時",
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // 投資報酬率
                        Row {
                            Text("投資報酬率：", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "${record.roi} %",
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // 備註
                        if (!record.note.isNullOrBlank()) {
                            Text("備註：", fontWeight = FontWeight.SemiBold)
                            Text(record.note, color = Color.DarkGray)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                // 確認對話框
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("刪除", color = Color.White)
                            }

                            Button(
                                onClick = { selectedRecord = null },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("完成")
                            }
                        }
                    }
                }
            }
        }
    }

    // 刪除確認對話框
    if (showDeleteDialog && selectedRecord != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("確認刪除") },
            text = { Text("您確定要永久刪除這筆紀錄嗎？此操作無法復原。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // 確定刪除時，執行刪除邏輯並關閉所有視窗
                        viewModel.deleteRecord(selectedRecord!!)
                        selectedRecord = null
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("確定刪除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}