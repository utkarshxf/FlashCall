package com.example.myapplication.myapplication.flashcall.Screens.wallet

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.myapplication.myapplication.flashcall.ViewModel.wallet.WalletViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WalletScreen(navController: NavController, walletViewModel: WalletViewModel = hiltViewModel())
{

    val transactions = walletViewModel.transactions.collectAsState()
    val listOfTransactions = transactions.value.transactions

    Log.d("WalletScreen", "WalletScreen: $listOfTransactions")


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
                        text = "Sujit Kumar",
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
                        text = "₹100,000.00",
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
                Log.d("TransactionGroup", "TransactionGroup: ${groupedTransaction[it]}")
            }
        }
    }

}

@Composable
fun TransactionGroupItem(transactionGroup: TransactionGroup){

//  val date = transactionGroup.date?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp)),
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(Color.White)
        ) {

            Text(
                text = "date",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                )
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                transactionGroup.transactions?.size?.let {
                    items(it){
                        TransactionItem(transactionGroup.transactions[it])
                        Log.d("TransactionGroupItem", "TransactionGroupItem: ${transactionGroup.transactions[it]}")
                    }
                }
            }

        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction)
{
    val date = LocalDateTime.parse(transaction.createdAt, DateTimeFormatter.ISO_DATE_TIME).toLocalDate() // Parse with timezone offset 'Z'
    Column(modifier = Modifier.heightIn(max = 200.dp)) {
        Text("Date: ${date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))}", modifier = Modifier.weight(1f)) // Format the date
        Log.d("TransactionItem", "TransactionItem: $date")
        Text("Amount: ${transaction.amount}", modifier = Modifier.weight(1f))
        Log.d("TransactionItem", "TransactionItem: ${transaction.amount}")
        Text("Type: ${transaction.type}", modifier = Modifier.weight(1f))
        Log.d("TransactionItem", "TransactionItem: ${transaction.type}")
        Text("User ID: ${transaction.userId}", modifier = Modifier.weight(1f))
        Log.d("TransactionItem", "TransactionItem: ${transaction.userId}")
    }


}