package com.bikulwon.manseryeok.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bikulwon.manseryeok.page.ui.theme.ManseryeokTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class InquiryComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManseryeokTheme {
                InquiryScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("문의하기") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .background(Color.White)
                    .height(56.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "비결원에 문의하실 내용을 남겨주세요",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "답신을 원하신다면,\n이메일 혹은 전화번호 중 하나를 남겨주세요",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            InquiryTextField(label = "제목", hint = "제목을 입력하세요")
            InquiryTextField(label = "이메일", hint = "이메일을 입력하세요", inputType = "email")
            InquiryTextField(label = "전화번호", hint = "전화번호를 입력하세요", inputType = "phone")
            InquiryTextField(label = "내용", hint = "내용을 입력하세요", minLines = 5)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle send action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("전송")
            }
        }
    }
}

@Composable
fun InquiryTextField(label: String, hint: String, inputType: String = "text", minLines: Int = 1) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(hint) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp),
            singleLine = minLines == 1,
            maxLines = if (minLines > 1) Int.MAX_VALUE else 1
        )
    }
}