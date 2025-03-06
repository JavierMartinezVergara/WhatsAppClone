package com.example.whatsappclone.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chat.ui.ChatScreen
import com.example.conversations.ui.ConversationsList
import com.example.create_chat.ui.CreateConversationScreen
import com.example.framework.ui.NavRoutes

@Composable
fun WhatAppCloneNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavRoutes.ConversationsList) {
        addConversationsList(navController)
        addConversationsList(navController)
        addChat(navController)
    }
}

private fun NavGraphBuilder.addConversationsList(
    navHostController: NavHostController
) {
    composable(NavRoutes.ConversationsList) {
        ConversationsList(onNewConversationClick = {
            navHostController.navigate(
                NavRoutes.NewConversation
            )
        }, onConversationClick = { chatId ->
            navHostController.navigate(
                NavRoutes.Chat.replace("{chatId}", chatId)
            )
        })
    }
}

private fun NavGraphBuilder.addNewConversation(
    navHostController: NavHostController
) {
    composable(NavRoutes.NewConversation) {
        CreateConversationScreen(onCreateConversation = {
            navHostController.navigate(NavRoutes.Chat)
        })
    }
}

private fun NavGraphBuilder.addChat(
    navHostController: NavHostController
) {
    composable(
        route = NavRoutes.Chat,
        arguments = listOf(
            navArgument(
                NavRoutes.ChatArgs.ChatId
            ) {
                type = NavType.StringType
            })
    ) { backStackEntry ->
        val chatId = backStackEntry.arguments?.getString(NavRoutes.ChatArgs.ChatId).orEmpty()
        ChatScreen(chatId = chatId, onBack = {
            navHostController.popBackStack()
        })
    }
}