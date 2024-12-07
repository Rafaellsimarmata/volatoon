package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.R
import com.example.volatoon.model.DetailComic
import com.example.volatoon.viewmodel.ComicViewModel

@Composable

fun UserActivityScreen(

    navController: NavController
){
    val navigateToDetail: (String) -> Unit = {
        navController.navigate("detail/$it")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Notifications >",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
        LazyColumn {
            items(2) {
                Text("New Notification")
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    navController.navigate("history")
                }
        ) {
            Text(
                text = "History >",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(5) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .clickable { navigateToDetail(comic.komik_id) }
                        .padding(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.comic_thumbnail),
                            contentDescription = "Comic Cover - ",
                            modifier = Modifier
                                .width(100.dp)
                                .height(150.dp)
                        )

                        // Comic Details
                        Column(
//                            modifier = Modifier.fillMaxWidth().padding(8.dp)
//                                .clickable { navigateToDetail(comic.komik_id) },
//                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
//                            Image(
//                                painter = rememberAsyncImagePainter(model = comic.image),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .width(119.dp).height(153.dp)
//                                    .aspectRatio(1f)
//                            )
//                            val displayTitle = if (comic.title.length > 20) {
//                                comic.title.take(12) + "..."
//                            } else {
//                                comic.title
//                            }
                            Text("Comic Title")
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    navController.navigate("bookmark")
                }
        ){
            Text(
                text = "Bookmarks >",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(5) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
//                            .clickable { navigateToDetail(comic.komik_id) }
                        .padding(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.comic_thumbnail),
                            contentDescription = "Comic Cover - ",
                            modifier = Modifier
                                .width(100.dp)
                                .height(150.dp)
                        )

                        // Comic Details
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
//                                .clickable { navigateToDetail(comic.komik_id) },
//                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
//                            Image(
//                                painter = rememberAsyncImagePainter(model = comic.image),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .width(119.dp).height(153.dp)
//                                    .aspectRatio(1f)
//                            )
//                            val displayTitle = if (comic.title.length > 20) {
//                                comic.title.take(12) + "..."
//                            } else {
//                                comic.title
//                            }
                            Text("Comic Title")
                        }
                    }
                }
            }
        }
    }
}

//@Preview (showBackground = true)
//@Composable
//fun PreviewScreen(){
//    UserActivityScreen()
//}
