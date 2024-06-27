package com.example.myapplication.myapplication.flashcall.Screens.profileOptions

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.BottomBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PaymentSettings(
    navController : NavController
){

    var upiRadioButton by remember {
        mutableStateOf(false)
    }
    var bankRadioButton by remember {
        mutableStateOf(false)
    }

    var selectedPaymentMethod by remember {
        mutableStateOf("")
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(BottomBackground),
        color = BottomBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ){

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .height(30.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ){

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(28.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ){
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "Payment Settings",
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            ){
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    RadioButton(
                        selected = upiRadioButton,
                        onClick = {
                            upiRadioButton = !upiRadioButton
                            bankRadioButton = false
                            selectedPaymentMethod = "UPI"
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor= Color.Black,
                            unselectedColor = Color.Unspecified,
                            disabledSelectedColor = Color.Unspecified,
                            disabledUnselectedColor= Color.Unspecified
                        )
                    )

                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "UPI")
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            ){
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    RadioButton(
                        selected = bankRadioButton,
                        onClick = {
                            bankRadioButton = !bankRadioButton
                            upiRadioButton = false
                            selectedPaymentMethod = "Bank Transfer / NEFT"
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor= Color.Black,
                        unselectedColor = Color.Unspecified,
                    disabledSelectedColor = Color.Unspecified,
                    disabledUnselectedColor= Color.Unspecified
                        )
                    )

                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Bank Transfer / NEFT")
                }

            }

            Spacer(modifier = Modifier.height(10.dp))

            if(selectedPaymentMethod.equals("UPI")){
                UpiPaymentBlock()
            }else if(selectedPaymentMethod.equals("Bank Transfer / NEFT")){
                BankPaymentBlock()
            }else{

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Save", color = Color.White)
                }
            }




        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UpiPaymentBlock() {


    var upiId by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                text = "UPI ID",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                shape = RoundedCornerShape(10.dp),
                value = upiId,
                onValueChange = { upiId = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
                placeholder = {
                    Text(
                        text = "Enter Your UPI ID",
                        color = SecondaryText,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MainColor
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankPaymentBlock() {

    var ifscNumber by remember {
        mutableStateOf("")
    }
    var accountNumber by remember {
        mutableStateOf("")
    }
    var accountType by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var textFiledSize by remember {
        mutableStateOf(Size.Zero)

    }

    var listOfAccountType = listOf("Saving", "Current")

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "IFSC Code",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                shape = RoundedCornerShape(10.dp),
                value = ifscNumber,
                onValueChange = { ifscNumber = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
                placeholder = {
                    Text(
                        text = "Enter Your IFSC Code",
                        color = SecondaryText,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MainColor
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Account Number",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                shape = RoundedCornerShape(10.dp),
                value = accountNumber,
                onValueChange = { accountNumber = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
                placeholder = {
                    Text(
                        text = "Enter Your Account Number",
                        color = SecondaryText,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MainColor
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Account Type",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))



            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    shape = RoundedCornerShape(10.dp),
                    value = accountType,
                    onValueChange = { accountType = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .background(color = Color.White)
                        .onGloballyPositioned { coordinates ->
                            textFiledSize = coordinates.size.toSize()

                        }
                        .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    readOnly = true,
                    placeholder = {
                        Text(
                            text = "Select Your Account Type",
                            color = SecondaryText,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = MainColor
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.background(Color.White)
                ) {
                    listOfAccountType.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                accountType = item
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }


            }
        }
    }
}
