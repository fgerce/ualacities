package com.example.ualachallenge.ui.screen

sealed class ScreenState {
    data object Loading : ScreenState()
    data class Error(val message: String) : ScreenState()
    data object Success : ScreenState()
}
