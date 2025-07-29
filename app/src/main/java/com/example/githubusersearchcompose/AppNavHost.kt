package com.example.githubusersearchcompose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.user_details.UserDetailScreen
import com.example.feature.user_list.UserListScreen


@Composable
fun AppNavHost(innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "user_list"
    ) {
        composable("user_list") {
            UserListScreen(onUserClick = { user ->
                navController.navigate("details/${user}")
            })
        }

        composable("details/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            UserDetailScreen(username = username, onBack = { navController.popBackStack() })
        }
    }
}

