package com.androidtechmanageapp.view.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androidtechmanageapp.viewmodel.TechViewModel

//3つの画面による遷移
@Composable
fun NavigationTop(viewModel: TechViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "top") {
        composable("top") {
            TopScreen(
                onNavigateToInsert = { navController.navigate("insert") },
                onNavigateToDetail = { navController.navigate("detail") },
                viewModel
            )
        }
        composable("insert") {
            InsertScreen(
                onNavigateToTop = { navController.popBackStack("top", inclusive = false) },
                viewModel
            )
        }
        composable("detail") {
            DetailScreen(
                onNavigateToTop = { navController.popBackStack("top", inclusive = false) },
                viewModel
            )
        }
    }
}