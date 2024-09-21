package com.example.myapplication.myapplication.flashcall.Screens

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.KycViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KYCScreen(
    navController: NavController,
    hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>,
    viewModel: KycViewModel = hiltViewModel()
) {
    var panExpanded by remember { mutableStateOf(false) }
    var aadhaarExpanded by remember { mutableStateOf(false) }
    var livelinessExpanded by remember { mutableStateOf(false) }
    var panState = viewModel.panState
    var aadharState = viewModel.aadharState
    var livelinessState = viewModel.livelinessState
    fun getImageResource(state: KycViewModel.VerificationState): Int {
        return when {
            state.verified -> R.drawable.baseline_verified_24
            state.error != null -> R.drawable.exclamation1
            else -> R.drawable.exclamation1
        }
    }
    val panImageResource = getImageResource(panState)
    val aadharImageResource = getImageResource(aadharState)
    val livelinessImageResource = getImageResource(livelinessState)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "KYC Documents",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            KYCSection("PAN", panExpanded, { panExpanded = !panExpanded },panImageResource) {
                PANContent(viewModel)
            }
            KYCSection("Aadhaar",aadhaarExpanded, { aadhaarExpanded = !aadhaarExpanded },aadharImageResource) {
                AadhaarContent(viewModel)
            }
            KYCSection("Liveliness Check", livelinessExpanded,{ livelinessExpanded = !livelinessExpanded },livelinessImageResource) {
                LivenessCheckContent(viewModel)
            }
        }
    }
}

@Composable
fun KYCSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    iconResId: Int,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = Color.Gray
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        }
    }
}

@Composable
fun KYCSection(
    title: String,
    iconResId: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                .clickable(onClick = onToggle)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null
                )

                Text(
                    text = title,
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun PANContent(viewModel: KycViewModel) {
    var panNumber by remember { mutableStateOf("") }
    OutlinedTextField(
        value = panNumber,
        onValueChange = { panNumber = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Enter PAN") },
        singleLine = true
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { viewModel.panVerification(panNumber) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Verify", color = Color.White)
    }
}

@Composable
fun AadhaarContent(viewModel: KycViewModel) {
    var aadhaarNumber by remember { mutableStateOf("") }

    OutlinedTextField(
        value = aadhaarNumber,
        onValueChange = { aadhaarNumber = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Enter your Aadhaar Number") },
        singleLine = true
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { viewModel.aadharVerification(aadhaarNumber) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Get OTP", color = Color.White)
    }
}

@Composable
fun LivenessCheckContent(viewModel: KycViewModel) {
    var fileName by remember { mutableStateOf("No file chosen") }

    Button(
        onClick = {  },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Choose File", color = Color.Black)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(fileName, color = Color.Gray)
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { /* Verify logic */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Verify", color = Color.White)
    }
}

@Preview
@Composable
fun KycScreenPreview() {
    //panVerification(vm)
    //KYCScreen(navController = rememberNavController())
}
