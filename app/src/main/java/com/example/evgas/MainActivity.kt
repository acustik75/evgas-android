package com.example.evgas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evgas.ui.screens.*
import com.example.evgas.ui.theme.EVGASTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EVGASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EVGASApp()
                }
            }
        }
    }
}

@Composable
fun EVGASApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "comparison"
    ) {
        composable("comparison") {
            ComparisonScreen(
                onElectricClick = { navController.navigate("electric") },
                onGasClick = { navController.navigate("gas") }
            )
        }
        composable("electric") {
            ElectricScreen(
                onArchiveClick = { navController.navigate("electric_archive") },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("gas") {
            GasScreen(
                onArchiveClick = { navController.navigate("gas_archive") },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("electric_archive") {
            ArchiveScreen(
                type = "electric",
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("gas_archive") {
            ArchiveScreen(
                type = "gas",
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}