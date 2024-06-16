package com.example.myapplication.myapplication.flashcall.Screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomBar
//import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
//import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
import com.example.myapplication.myapplication.flashcall.bottomnav.Screen
//import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.BottomBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.SwitchColor
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import java.time.LocalDate


var uid:String? = null
@Composable
fun HomeScreen(navController: NavController, registrationViewModel: RegistrationViewModel)
{

    val createUserState by registrationViewModel.createUserState.collectAsState()

    when(createUserState)
    {
        is APIResponse.Success->{
            val response = (createUserState as APIResponse.Success).data
            uid = response._id
            Log.e("UserId", uid.toString())
        }

        APIResponse.Empty -> Log.e("EmptyError", "empty")
        is APIResponse.Error -> {
            Log.e("Error", "Error Failed")
        }
        APIResponse.Loading -> Log.e("Loading", "Loading")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {

        Scaffold(
//            bottomBar = { BottomBar(navController = navController) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.Black)
            ) {

                //BottomNavGraph(navController = navController, registrationViewModel = registrationViewModel)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(30.dp)
                        .height(50.dp)
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.Absolute.Right,
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.White),
                            onClick = { navController.navigate(ScreenRoutes.EditScreen.route) }
                        ) {
                            Text(
                                text = "edit profile",
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.home_image),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(90.dp)
                        )
                    }

                }

                Text(
                    text = "Nitra Sahgal",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sahgal55@consultant",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                HomeScreenBottom()


            }
        }

    }

}









@Composable
fun HomeScreenBottom()
{

    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    PrimaryBackGround,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {

                    CopyBar()

                    Image(painter = painterResource(id = R.drawable.share_icon),
                        modifier = Modifier
                            .padding(start = 2.dp, top = 2.dp)
                            .size(42.dp)
                            .clickable {

                            },
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                WalletBar()

                Spacer(modifier = Modifier.height(10.dp))

                ServicesSection()


            }
        }
    }
}

@Composable
fun CopyBar()
{
    var context = LocalContext.current
    Row(
        modifier = Modifier
            .width(300.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(24.dp))
    )
    {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .border(1.dp, BorderColor2, shape = RoundedCornerShape(24.dp)))

        {
            Row(modifier = Modifier.fillMaxSize())
            {
                Image(painter = painterResource(id = R.drawable.link_icon),
                    modifier = Modifier.padding(16.dp),
                    contentDescription = null)

                Text(text = "https://www.flashcall.me/nitra-sahgal-55-consultant",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterVertically),
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                )

                Image(painter = painterResource(id = R.drawable.copy_icon),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                                   copyToClipboard(
                                       context = context,
                                       "https://www.flashcall.me/nitra-sahgal-55-consultant"
                                   )
                        },
                    contentDescription = null)


            }

        }
    }
}

@Composable
fun WalletBar()
{


    Box(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .background(color = Color.White)
        .border(1.dp, BorderColor2, shape = RoundedCornerShape(10.dp))
    ){
        Row(modifier = Modifier.fillMaxSize())
        {
            Image(painter = painterResource(id = R.drawable.wallet_icon),
                modifier = Modifier.padding(16.dp),
                contentDescription = null)

            Column(modifier = Modifier.fillMaxHeight()) {

                Text(text = "Today's Earning",
                    modifier = Modifier.padding(top = 10.dp),
                    style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    color = Color.Black
                    )
                )

                Row {
                    Text(text = "Rs.",
                        modifier = Modifier.padding(top = 8.dp),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    )
                    Text(text = "10000",
                        modifier = Modifier.padding(top = 8.dp),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    )
                }

            }

            Spacer(Modifier.weight(1f))
            Button(modifier = Modifier
                .padding(top = 8.dp, end = 10.dp)
                .width(120.dp)
                .height(40.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainColor,
                    contentColor = Color.White
                ),
                onClick = {  }
            ) {

                Text(text = "View Wallet",
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                    ))

            }
        }
    }
}

@Composable
fun ServicesSection()
{
    var serviceSelected by remember {
        mutableStateOf(false)
    }

    var audioService by remember {
        mutableStateOf(false)
    }

    var videoService by remember {
        mutableStateOf(false)
    }

    var chatService by remember {
        mutableStateOf(false)
    }

    var audioPrice by remember {
        mutableStateOf("")
    }

    var chatPrice by remember {
        mutableStateOf("")
    }

    var videoPrice by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)
        .background(color = Color.White)
        .border(1.dp, BorderColor2, shape = RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(10.dp)
            )
            {

                Text(text = "My Services",
                    modifier = Modifier.padding(start=5.dp),
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.weight(1f))
                Switch(
                        checked = serviceSelected,
                    onCheckedChange =
                    {
                    serviceSelected = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MainColor,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = SwitchColor
                    ),
                    modifier = Modifier
                        .width(50.dp)
                        .padding(top = 5.dp)
                )


            }

            Spacer(modifier = Modifier.height(5.dp))

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))
            
            var isDialog by remember {
                mutableStateOf(false)
            }
            
            Dialog(onDismissRequest = { /*TODO*/ }) {
                
            }


            Column(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clickable {
                    isDialog = true
                })
            {

                Row(
                    modifier = Modifier
                        .height(50.dp)
                ){
                    Column(modifier = Modifier.fillMaxHeight())
                    {
                        Text(text = "Video Call",
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row()
                        {
                            Text(text = "Rs. 25/min",
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .size(16.dp)
                                    .clickable {

                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = videoService,
                        onCheckedChange =
                        {
                            videoService = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MainColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = SwitchColor
                        ),
                        modifier = Modifier
                            .width(50.dp)
                            .padding(bottom = 15.dp), enabled = serviceSelected
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.height(50.dp)

                ){
                    Column(modifier = Modifier.fillMaxHeight())
                    {
                        Text(text = "Audio Call",
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row()
                        {
                            Text(text = "Rs. 25/min",
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .size(16.dp)
                                    .clickable {

                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = audioService,
                        onCheckedChange =
                        {
                            audioService = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MainColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = SwitchColor
                        ),
                        modifier = Modifier
                            .width(50.dp)
                            .padding(bottom = 15.dp), enabled = serviceSelected
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.height(50.dp)

                ){
                    Column(modifier = Modifier.fillMaxHeight())
                    {
                        Text(text = "Chat",
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row()
                        {
                            Text(text = "Rs. 25/min",
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .size(16.dp)
                                    .clickable {

                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = chatService,
                        onCheckedChange =
                        {
                            chatService = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MainColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = SwitchColor
                        ),
                        modifier = Modifier
                            .width(50.dp)
                            .padding(bottom = 15.dp), enabled = serviceSelected
                    )
                }

            }
            
            if(isDialog)
                Dialog(onDismissRequest = { }) {

                }
        }
    }
}

@Composable
fun CustomToggleButton(selected:Boolean, onChangeValue:(Boolean)->Unit) {
    Card(
        modifier = Modifier.width(50.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier
            .background(
                if (selected) MainColor else Color.White
            )
            .clickable {
                onChangeValue(!selected)
            },
            contentAlignment = if(selected) Alignment.TopEnd else Alignment.TopStart
        ){
            CustomCheck(Modifier.padding(5.dp))
        }
    }
}
@Composable
fun CustomCheck(modifier: Modifier) {

    Card(
        modifier = modifier.size(20.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = CircleShape
    ) {
        Box(modifier = Modifier.background(Color.White))
    }

}


fun copyToClipboard(
    context : Context,
    copyText : String,

){
    val clipBoard = context.getSystemService(
        Context.CLIPBOARD_SERVICE
    ) as ClipboardManager

    val clip = ClipData.newPlainText(
        "Copied Text",
        copyText
    )
    clipBoard.setPrimaryClip(clip)
}

