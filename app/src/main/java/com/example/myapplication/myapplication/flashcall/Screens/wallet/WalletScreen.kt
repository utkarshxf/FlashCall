package com.example.myapplication.myapplication.flashcall.Screens.wallet

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionGroup
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.groupByDate
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.ShareTextButton
import com.example.myapplication.myapplication.flashcall.Screens.common.CircularLoaderButton
import com.example.myapplication.myapplication.flashcall.Screens.copyToClipboard
import com.example.myapplication.myapplication.flashcall.Screens.imageUrl
import com.example.myapplication.myapplication.flashcall.Screens.shareLink
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.wallet.WalletViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import com.example.myapplication.myapplication.flashcall.utils.LoadingIndicator
import com.example.myapplication.myapplication.flashcall.utils.capitalizeAfterSpace
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.contracts.contract
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun WalletScreen(
    navController: NavController,
    walletViewModel: WalletViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val userData = authenticationViewModel.getUserFromPreferences(context)
    val transactions = walletViewModel.transactions.collectAsState()
    val listOfTransactions = transactions.value.transactions
    LaunchedEffect(Unit) {
        userData?._id?.let {
            walletViewModel.fetchTransactions(it)
            walletViewModel.fetchBalance(it)
        }
    }
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
                            fontFamily = helveticaFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )

                    Text(
                        text = userData?.fullName ?: "User",
                        modifier = Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            fontFamily = helveticaFontFamily,
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
                    .border(1.dp, BorderColor, shape = RoundedCornerShape(16.dp)),

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
                            fontFamily = helveticaFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "₹${userData?.walletBalance?.roundToInt()}",
                        style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainColor
                        )
                    )

                }

            }

            val withdrawState = walletViewModel.requestWithdrawState

            Spacer(modifier = Modifier.height(10.dp))

            if (withdrawState.isLoading) {
                LoadingIndicator()
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (withdrawState.error != null) {
                Text(text = "error: ${withdrawState.error}", color = Color.Red, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (withdrawState.success) {
                Text(
                    text = "success: ${withdrawState.message}",
                    color = MainColor,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }


            Button(
                onClick = {
                    if(userData?.walletBalance?.roundToInt()!! > 500){
                        if (walletViewModel.isWithdrawable()) {
                            walletViewModel.withdrawRequest()
                        } else {
                            Toast.makeText(
                                context,
                                "Your payment setting or kyc is pending",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            context,
                            "Minimum withdraw balance is RS 500",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
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
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                )
            }


            Spacer(modifier = Modifier.height(10.dp))
            if (!listOfTransactions.isNullOrEmpty()) {
                Row {
                    Text(text = "Transaction History")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            if (listOfTransactions.isNullOrEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Add you link to your social media profile and start earning.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 30.dp)
                        )


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        )
                        {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .height(48.dp)
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .clickable {
                                        copyToClipboard(
                                            context = context,
                                            walletViewModel.getShareLink()
                                        )
                                        Toast
                                            .makeText(context, "Link Copied", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = BorderColor,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 20.dp)

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.copy_icon),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Copy Link",
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontFamily = helveticaFontFamily,
                                        fontWeight = FontWeight.Normal,
                                    ), color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))

                            WalletShareButton(
                                shareLink = walletViewModel.getShareLink(),
                                bio = walletViewModel.getMyBio()
                            )

                        }


                    }

                }
            } else {
                TransactionGroup(listOfTransactions)
            }

        }
    }
}

@Composable
fun WalletShareButton(shareLink: String, bio: String) {
    var sharingContent =
        "Hi 👋\n\nYou can use the below link to consult with me through Video Call, Audio Call or Chat. \n\nLink: $shareLink\n\n"

    if (bio.isNotEmpty()) {
        sharingContent += "About me: $bio"
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }

        }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(48.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .clickable {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, sharingContent
                    )
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                launcher.launch(shareIntent)
            }
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 20.dp)

    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_share_24),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Share Link",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = helveticaFontFamily,
                fontWeight = FontWeight.Normal,
            ), color = Color.Black
        )
    }
}


@Composable
fun TransactionGroup(transactions: List<Transaction>?) {

    val groupedTransaction = transactions?.groupByDate()?.sortedByDescending {
        it.date
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        groupedTransaction?.size?.let {
            items(it) {
                TransactionGroupItem(groupedTransaction[it])
            }
        }
    }

}

@Composable
fun TransactionGroupItem(transactionGroup: TransactionGroup) {

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
                    text = it.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        fontFamily = helveticaFontFamily
                    ),
                    color = SecondaryText,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
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
        Column {
            Row {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Transaction Icon",
                    modifier = Modifier
                        .size(45.dp)
                        .padding(end = 10.dp)
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
//                        .weight(1f)
                        ) {
                            Row {
                                Text(
                                    "Transaction ID ",
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = helveticaFontFamily,
                                    fontSize = 14.sp
                                )
                                Text(
                                    "${transaction._id.take(10)}..",
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = helveticaFontFamily,
                                    fontSize = 14.sp
                                )

                            }
                            val context = LocalContext.current
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = createdAtTime, fontSize = 12.sp, color = SecondaryText)
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.copy_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable {
                                            copyToClipboard(context, "${transaction}")
                                            Toast
                                                .makeText(context, "Copied", Toast.LENGTH_SHORT)
                                                .show()
                                        })
                            }

                        }

                        // Amount with Negative Sign if applicable
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 5.dp)
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


                }
            }
            if (!isLastInGroup) {
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)
                )
            }
        }

    }
}



