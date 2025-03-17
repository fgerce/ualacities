package com.example.ualachallenge.ui.main

import androidx.compose.runtime.Composable
import com.example.ualachallenge.ui.navigation.UalaNavHost
import com.example.ualachallenge.ui.theme.UalaTheme

@Composable
fun UalaApp() {
    UalaTheme {
        UalaNavHost()
    }
}
