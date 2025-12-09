package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ToolScreen() {
    var selectedTool by remember { mutableStateOf("亂數產生器") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 模式切換列
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("亂數產生器", "倒數計時器").forEach { mode ->
                val isSelected = selectedTool == mode
                Text(
                    text = mode,
                    color = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier
                        .background(
                            color = if (isSelected) Color(0xFF2196F3) else Color.LightGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedTool = mode }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 顯示對應工具
        when (selectedTool) {
            "亂數產生器" -> RandomSection()
            "倒數計時器" -> TimerSection()
        }
    }
}

// 亂數產生器
@Composable
fun RandomSection() {
    var number by remember { mutableStateOf(Random.nextInt(100)) }
    var isAuto by remember { mutableStateOf(false) }

    // 自動模式每 3 秒更新亂數
    LaunchedEffect(isAuto) {
        while (isAuto) {
            number = Random.nextInt(100)
            delay(3000)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("亂數產生器", fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))

        Card(
            modifier = Modifier
                .size(400.dp)
                .clickable {
                    if (!isAuto) {
                        number = Random.nextInt(100)
                    }
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "$number",
                    fontSize = 256.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 模式切換按鈕
        Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Button(
                onClick = { isAuto = true },
                shape = RoundedCornerShape(12.dp)
            ) { Text("自動") }

            Button(
                onClick = { isAuto = false },
                shape = RoundedCornerShape(12.dp)
            ) { Text("手動") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 模式顯示
        Text(
            text = if (isAuto) "目前模式：自動" else "目前模式：手動",
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 操作方法提示
        if (!isAuto) {
            Text(
                text = "手動模式下輕觸方塊即可生成隨機數",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        if (isAuto) {
            Text(
                text = "自動模式下每隔三秒自動生成隨機數",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

// 倒數計時器
@Composable
fun TimerSection() {
    var defaultTime by remember { mutableStateOf(30) }  // 預設倒數秒數
    var timeLeft by remember { mutableStateOf(defaultTime) }
    var isCounting by remember { mutableStateOf(true) }

    // 倒數邏輯
    LaunchedEffect(isCounting, timeLeft) {
        while (isCounting && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("倒數計時器", fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))

        // 灰底數字區塊（點擊可重設）
        Card(
            modifier = Modifier
                .size(400.dp)
                .clickable {
                    timeLeft = defaultTime
                    isCounting = true
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "$timeLeft",
                    fontSize = 256.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("點擊方塊即可重新倒數", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // 調整預設倒數秒數
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { if (defaultTime >= 10) defaultTime -= 10 },
                shape = RoundedCornerShape(12.dp)
            ) { Text("- 10s") }

            Button(
                onClick = { if (defaultTime < 90) defaultTime += 10 },
                shape = RoundedCornerShape(12.dp)
            ) { Text("+ 10s") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "目前預設時間：$defaultTime 秒",
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
