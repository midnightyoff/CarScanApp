package com.example.carapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carapp.presentation.ObdConnectionViewModel
import com.example.carapp.presentation.mainviewmodel.MainViewModel
import com.example.carapp.ui.navigation.BottomNavBar
import com.example.carapp.ui.navigation.Screen
import com.example.carapp.ui.screens.errorscreen.ErrorCodesScreen
import com.example.carapp.ui.screens.journal.JournalScreen
import com.example.carapp.ui.screens.mainscreen.MainScreen
import com.example.carapp.ui.screens.terminal.TerminalScreen
import com.example.carapp.ui.screens.settings.SettingsScreen
import com.example.carapp.ui.screens.showcurrentdata.ShowCurrentDataScreen
import com.example.carapp.ui.theme.CarAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val obdViewModel: ObdConnectionViewModel = viewModel()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(padding),
            route = "root"
        ) {
            // TODO try catch
            composable(Screen.Journal.route) { val mainViewModel: MainViewModel = viewModel()
                JournalScreen(carId = mainViewModel.selectedCar.value?.id)  }
            composable(Screen.Main.route) { MainScreen(navController, obdViewModel) }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.Terminal.route) { TerminalScreen(navController, obdViewModel) }
            composable(Screen.ErrorCodes.route) { ErrorCodesScreen(navController, obdViewModel) }
            composable(Screen.ShowCurrentData.route) { ShowCurrentDataScreen(navController, obdViewModel) }
        }
    }
}



