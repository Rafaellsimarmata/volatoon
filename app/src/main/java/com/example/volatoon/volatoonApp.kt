package com.example.volatoon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.volatoon.view.DashboardScreen
import com.example.volatoon.view.DetailChapterScreen
import com.example.volatoon.view.DetailComicScreen
import com.example.volatoon.view.NotificationsScreen
import com.example.volatoon.view.ProfileScreen
import com.example.volatoon.view.SearchScreen
import com.example.volatoon.view.TrendingScreen
import com.example.volatoon.viewmodel.ComicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolatoonApp(navController: NavHostController){

    val comicViewModel : ComicViewModel = viewModel()
    val viewState by comicViewModel.comicstate

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            NavHost(navController = navController, startDestination = TopLevelRoute.Dashboard.route){
                composable(route = TopLevelRoute.Dashboard.route){
                    DashboardScreen(viewState = viewState,
                        navigateToDetail = { comicId ->
                         navController.navigate(route = "detailcomic/$comicId")
                    })
                }

                composable(route = TopLevelRoute.Trending.route){
                    TrendingScreen()
                }

                composable(route = TopLevelRoute.Search.route){
                    SearchScreen()
                }

                composable(route = TopLevelRoute.Notifications.route){
                    NotificationsScreen()
                }

                composable(route = TopLevelRoute.Profile.route){
                    ProfileScreen()
                }

                composable("detailcomic/{comicId}") {
                    val comicId = it.arguments?.getString("comicId") ?: ""

                    val detailComicState by comicViewModel.detailComicState

                    // Use LaunchedEffect to ensure fetchDetailComic is called only once
                    LaunchedEffect(comicId) {
                        comicViewModel.fetchDetailComic(comicId)
                    }

                    Box(modifier = Modifier.fillMaxSize()){
                        when {
                            detailComicState.loading -> {
                                CircularProgressIndicator(progress = 0.89f, modifier = Modifier.align(Alignment.Center))
                            }
                            detailComicState.detailComic != null -> {
                                DetailComicScreen(detailComicState, navigateToDetail = { chapterId ->
                                    navController.navigate(route = "detailchapter/$chapterId")
                                })
                            }

                            detailComicState.error != null -> {
                                Text(text = detailComicState.error!!)
                            }
                        }
                    }
                }

                composable("detailchapter/{chapterId}") {
                    val chapterId = it.arguments?.getString("chapterId") ?: ""
                    val detailChapterState by comicViewModel.detailChapterState

                    LaunchedEffect(chapterId) {
                        comicViewModel.fetchDetailChapter(chapterId)
                    }

                    Box(modifier = Modifier.fillMaxSize()){
                        when {
                            detailChapterState.loading -> {
                                CircularProgressIndicator(progress = 0.89f, modifier = Modifier.align(Alignment.Center))
                            }
                            detailChapterState.detailChapter != null -> {
                                DetailChapterScreen(detailChapterState)
                            }

                            detailChapterState.error != null -> {
                                Text(text = detailChapterState.error!!)
                            }
                        }
                    }


                }
            }
        }
    }



}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        TopLevelRoute.Dashboard,
        TopLevelRoute.Trending,
        TopLevelRoute.Search,
        TopLevelRoute.Notifications,
        TopLevelRoute.Profile
    )

    NavigationBar  {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = "${item.label} icon")
                       },
                label = { Text(item.label) },
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}