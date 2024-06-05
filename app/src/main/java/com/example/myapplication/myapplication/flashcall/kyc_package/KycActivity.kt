package com.example.myapplication.myapplication.flashcall.kyc_package

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import co.hyperverge.hyperkyc.HyperKyc
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import co.hyperverge.hyperkyc.data.models.result.HyperKycStatus
import com.example.myapplication.myapplication.flashcall.kyc_package.ui.theme.FlashCallTheme

class KycActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var config = HyperKycConfig(
            appId = "muzdob",
            appKey = "2ns9u1evoeugbrydykl7",
            workflowId = "workflow_9KW4mUl",
            transactionId = "TestTransact6",
        )
        val hyperKycLauncher = registerForActivityResult(HyperKyc.Contract()) { result ->

            // handle result post workflow finish/exit

            when(result.status){

                HyperKycStatus.AUTO_APPROVED->{
                    Log.d("HyperKyc", "Auto Approved")


                }

                HyperKycStatus.ERROR->{
                    Log.d("HyperKyc", "Error")

                }

                HyperKycStatus.NEEDS_REVIEW->{
                    Log.d("HyperKyc", "Needs Review")

                }

                HyperKycStatus.AUTO_DECLINED->{
                    Log.d("HyperKyc", "AUTO_DECLINED")


                }

                HyperKycStatus.USER_CANCELLED->{
                    Log.d("HyperKyc", "User_Declined")

                }


            }

            val data = result.details
            Log.d("HyperKyc", "Data: $data")
            Log.e("HyperKyc", "Status: ${result.transactionId}")
        }

        hyperKycLauncher.launch(config)
        setContent {
            FlashCallTheme {



            }
        }
    }
}

fun kyc()
{

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlashCallTheme {
        Greeting("Android")
    }
}