package com.example.whatsappclone.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclone.MainScreen

@Composable
fun WhatAppCloneNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "start_screen") {
        composable("start_screen") {
            MainScreen()
        }
    }
}