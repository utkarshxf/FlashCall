package com.example.myapplication.myapplication.flashcall.Screens.wallet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionGroup
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.groupByDate
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.wallet.WalletViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@Composable
fun WalletScreen(navController: NavController, walletViewModel: WalletViewModel = hiltViewModel(), registrationViewModel: RegistrationViewModel, authenticationViewModel: AuthenticationViewModel = hiltViewModel())
{
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val userData = authenticationViewModel.getUserFromPreferences(context)
    var name: String? = userData?.fullName
    if (name == null) {
        name = registrationViewModel.getStoredUserData("fullName")
    }
    LaunchedEffect(Unit) {
        userData?._id.let {
            walletViewModel.fetchTransactions(it.toString())
        }
        userData?._id.let {
            walletViewModel.getUserDetails(it.toString())
        }
    }
    val transactions = walletViewModel.transactions.collectAsState()
    val userDetails = walletViewModel.userDetails.collectAsState()
    val listOfTransactions = transactions.value.transactions
//    val transactions = listOf(
//        Transaction(
//            _id = "546332145",
//            userId = "User1",
//            userTypeId = "Type1",
//            amount = 1500.0,
//            type = "Credit",
//            createdAt = "2023-07-15T10:00:00Z",
//            updatedAt = "2023-07-15T10:00:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User13",
//            userTypeId = "Type2",
//            amount = -500.0,
//            type = "Debit",
//            createdAt = "2023-07-15T10:00:00Z",
//            updatedAt = "2023-07-15T10:00:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User2",
//            userTypeId = "Type2",
//            amount = -500.0,
//            type = "Debit",
//            createdAt = "2023-07-16T12:30:00Z",
//            updatedAt = "2023-07-16T12:30:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User1",
//            userTypeId = "Type1",
//            amount = 250.0,
//            type = "Credit",
//            createdAt = "2023-07-17T08:45:00Z",
//            updatedAt = "2023-07-17T08:45:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User3",
//            userTypeId = "Type3",
//            amount = -200.0,
//            type = "Debit",
//            createdAt = "2023-07-18T14:15:00Z",
//            updatedAt = "2023-07-18T14:15:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User2",
//            userTypeId = "Type2",
//            amount = 3200.0,
//            type = "Credit",
//            createdAt = "2023-07-19T11:00:00Z",
//            updatedAt = "2023-07-19T11:00:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User3",
//            userTypeId = "Type3",
//            amount = -1500.0,
//            type = "Debit",
//            createdAt = "2023-07-20T09:30:00Z",
//            updatedAt = "2023-07-20T09:30:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User1",
//            userTypeId = "Type1",
//            amount = 500.0,
//            type = "Credit",
//            createdAt = "2023-07-21T07:00:00Z",
//            updatedAt = "2023-07-21T07:00:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User2",
//            userTypeId = "Type2",
//            amount = -800.0,
//            type = "Debit",
//            createdAt = "2023-07-22T13:45:00Z",
//            updatedAt = "2023-07-22T13:45:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User3",
//            userTypeId = "Type3",
//            amount = 2200.0,
//            type = "Credit",
//            createdAt = "2023-07-23T10:15:00Z",
//            updatedAt = "2023-07-23T10:15:00Z",
//            __v = 0
//        ),
//        Transaction(
//            _id = "546332145",
//            userId = "User1",
//            userTypeId = "Type1",
//            amount = -900.0,
//            type = "Debit",
//            createdAt = "2023-07-24T15:00:00Z",
//            updatedAt = "2023-07-24T15:00:00Z",
//            __v = 0
//        )
//    )



//    Log.d("WalletScreen", "WalletScreen: $listOfTransactions")


    Surface(
        modifier = Modifier.wrapContentSize(),
        color = ProfileBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {

                Column {

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "Welcome,",
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    )

                    Text(
                        text = userDetails.value.fullName?:"User",
                        modifier = Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp)),

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = "Wallet Balance",
                        modifier = Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "₹${userDetails.value.walletBalance}",
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainColor
                        )
                    )

                }

            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainColor,
                    contentColor = Color.White
                )
            ) {

                Text(
                    text = "Withdraw",
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                )
            }


            Spacer(modifier = Modifier.height(10.dp))
//            if (listOfTransactions.isNullOrEmpty()) {
//                Text(text = "No transactions available")
//            }
            Text(text = "Transition History", modifier = Modifier.padding(end = 225.dp))
            Spacer(modifier = Modifier.height(8.dp))

            TransactionGroup(listOfTransactions)
        }
    }
}

@Composable
fun TransactionGroup(transactions: List<Transaction>?){

    val groupedTransaction = transactions?.groupByDate()?.sortedByDescending {
        it.date
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        groupedTransaction?.size?.let {
            items(it){
                TransactionGroupItem(groupedTransaction[it])
            }
        }
    }

}

@Composable
fun TransactionGroupItem(transactionGroup: TransactionGroup){

//  val date = transactionGroup.date?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .clip(RoundedCornerShape(16.dp))
//            .background(Color.White)
//            .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp)),
//    ){
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//                .background(Color.White)
//        ) {
//
//            Text(
//                text = "date",
//                style = TextStyle(
//                    fontFamily = arimoFontFamily,
//                    fontWeight = FontWeight.Light,
//                    fontSize = 16.sp,
//                )
//            )
//
//            LazyColumn(
//                modifier = Modifier.heightIn(max = 300.dp)
//            ) {
//                transactionGroup.transactions?.size?.let {
//                    items(it){
//                        TransactionItem(transactionGroup.transactions[it])
//                        Log.d("TransactionGroupItem", "TransactionGroupItem: ${transactionGroup.transactions[it]}")
//                    }
//                }
//            }
//
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(16.dp))

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(Color.White)
        ) {
            transactionGroup.date?.let {
                Text(
                    text = it.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                    style = TextStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            transactionGroup.transactions?.forEachIndexed { index, transaction ->
                val isLastInGroup = index == transactionGroup.transactions.size - 1
                TransactionItem(transaction = transaction, isLastInGroup = isLastInGroup)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, isLastInGroup: Boolean) {
    val createdAtTime = transaction.createdAt?.let {
        LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern("hh:mm a")) // 12-hour format with AM/PM
    } ?: "N/A"
    val amount = transaction.amount ?: 0.0
    val displayAmount = abs(amount)
    val isPositive = transaction.amount?.let { it > 0 } ?: false
    val imageRes = if (isPositive) R.drawable.group_60 else R.drawable.group_64
    val amountColor = if (isPositive) Color(MainColor.value) else Color.Red

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Transaction Icon
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Transaction Icon",
            modifier = Modifier
                .size(45.dp)
                .padding(end = 8.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Transaction ID: ${transaction._id}", fontWeight = FontWeight.Bold)
                    Text(text = createdAtTime, fontSize = 14.sp)
                }

                // Amount with Negative Sign if applicable
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (transaction.type.equals("debit")) {
                        Text(
                            text = "-",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 18.sp),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }

                    Text(
                        text = "+₹${"%.2f".format(displayAmount)}",
                        color = amountColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!isLastInGroup) {
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}



