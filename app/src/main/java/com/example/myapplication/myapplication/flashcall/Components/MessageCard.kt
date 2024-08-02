import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ReceiverMessageCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 8.dp, bottomEnd = 16.dp)

    ) {
        Column(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max)
                .padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
            Text(
                text = text,
                maxLines = 3,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun SenderMessageCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ){
        Card(
            modifier = modifier
                .padding(8.dp)
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Max),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 8.dp)

        ) {

            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .wrapContentWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }
//                Text(
//                    text = text,
//                    maxLines = 3,
//                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
//                )

                AsyncImage(
                    model = "https://upload.wikimedia.org/wikipedia/commons/b/bc/Alan_Walker_%28cropped%29.jpg",
                    contentDescription = "" ,
                    modifier = Modifier.size(100.dp)
                )
            }


        }
    }

}
