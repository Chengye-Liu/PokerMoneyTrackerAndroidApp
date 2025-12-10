package com.example.myapplication.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.RecordsViewModel
import com.example.myapplication.model.PokerRecordEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@Composable
fun AddRecordScreen(navController: NavController, viewModel: RecordsViewModel = viewModel()) {
    val context = LocalContext.current
    val formatter = remember { SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()) }

    var selectedType by remember { mutableStateOf("現金") }
    var gameName by remember { mutableStateOf("") }
    var buyIn by remember { mutableStateOf("") }
    var cashOut by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("請選擇") }
    var endTime by remember { mutableStateOf("請選擇") }

    fun showDateTimePicker(onResult: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                calendar.set(year, month, day, hour, minute)
                onResult(formatter.format(calendar.time))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("新增紀錄", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // 類型切換
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("現金", "錦標").forEach { type ->
                val isSelected = selectedType == type
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (isSelected) Color(0xFF00C853) else Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedType = type }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(type, fontSize = 16.sp, color = Color.Black)
                }
            }
        }

        // 時間選擇器
        RecordField(label = "上桌時間：", value = startTime, onClick = { showDateTimePicker { startTime = it } })
        RecordField(label = "離桌時間：", value = endTime, onClick = { showDateTimePicker { endTime = it } })

        // 遊戲名稱
        TextInputField(label = "遊戲名稱：", value = gameName) { gameName = it }

        // 金額
        NumberInputField(label = "買入：", value = buyIn) { buyIn = it }
        NumberInputField(label = "買出：", value = cashOut) { cashOut = it }

        // 備註
        Text("備註：", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        TextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFDCDCDC),
                focusedContainerColor = Color(0xFFDCDCDC)
            ),
            placeholder = { Text("輸入備註") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 儲存 + 取消
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("取消", color = Color.White)
            }

            Button(
                onClick = {
                    val buy = buyIn.toIntOrNull() ?: 0
                    val cash = cashOut.toIntOrNull() ?: 0
                    val profit = cash - buy
                    val roi = if (buy != 0) ((profit.toFloat() / buy) * 100).toInt() else 0

                    val start = try { formatter.parse(startTime) } catch (_: Exception) { null }
                    val end = try { formatter.parse(endTime) } catch (_: Exception) { null }

                    val duration: String
                    val hourly: Int

                    if (start != null && end != null && end.after(start)) {
                        val diffMillis = end.time - start.time
                        val minutes = (diffMillis / 1000 / 60).toInt()
                        val hours = minutes / 60
                        val mins = minutes % 60
                        duration = "${hours}小時${mins}分"
                        val totalHours = minutes.toFloat() / 60f
                        hourly = if (totalHours > 0) (profit / totalHours).roundToInt() else 0
                    } else {
                        duration = "--"
                        hourly = 0
                    }

                    val newRecord = PokerRecordEntity(
                        type = selectedType,
                        gameName = gameName.ifBlank { "未命名遊戲" },
                        buyIn = buy,
                        cashOut = cash,
                        profit = profit,
                        hourly = hourly,
                        roi = roi,
                        date = startTime,
                        endDate = endTime,
                        duration = duration,
                        note = note
                    )
                    viewModel.addRecord(newRecord)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("儲存", color = Color.White)
            }
        }
    }
}

@Composable
fun RecordField(label: String, value: String, onClick: (() -> Unit)? = null) {
    Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDCDCDC), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .let { if (onClick != null) it.clickable { onClick() } else it }
    ) {
        Text(value, color = Color.DarkGray)
    }
}

/* 可輸入文字（非數字）的欄位 */
@Composable
fun TextInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFDCDCDC),
            focusedContainerColor = Color(0xFFDCDCDC)
        ),
        placeholder = { Text("輸入名稱") },
        singleLine = true
    )
}

/* 數字輸入欄位 */
@Composable
fun NumberInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFDCDCDC),
            focusedContainerColor = Color(0xFFDCDCDC)
        ),
        placeholder = { Text("輸入金額") },
        singleLine = true
    )
}
