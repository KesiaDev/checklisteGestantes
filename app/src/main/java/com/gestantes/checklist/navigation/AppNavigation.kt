package com.gestantes.checklist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gestantes.checklist.billing.SubscriptionManager
import com.gestantes.checklist.data.entity.ChecklistCategory
import com.gestantes.checklist.data.preferences.UserData
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import com.gestantes.checklist.ui.checklist.ChecklistScreen
import com.gestantes.checklist.ui.diary.DiaryScreen
import com.gestantes.checklist.ui.documents.DocumentsScreen
import com.gestantes.checklist.ui.growth.GrowthScreen
import com.gestantes.checklist.ui.guides.CareGuideScreen
import com.gestantes.checklist.ui.guides.DevelopmentScreen
import com.gestantes.checklist.ui.guides.SleepGuideScreen
import com.gestantes.checklist.ui.history.HistoryScreen
import com.gestantes.checklist.ui.home.HomeScreen
import com.gestantes.checklist.ui.onboarding.OnboardingScreen
import com.gestantes.checklist.ui.search.SearchScreen
import com.gestantes.checklist.ui.settings.SettingsScreen
import com.gestantes.checklist.ui.subscription.SubscriptionScreen
import com.gestantes.checklist.ui.weekly.WeeklyChecklistScreen
import com.gestantes.checklist.ui.timeline.TimelineScreen
import com.gestantes.checklist.ui.content.PregnancyContentScreen

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Checklist : Screen("checklist/{category}") {
        fun createRoute(category: ChecklistCategory) = "checklist/${category.name}"
    }
    data object SleepGuide : Screen("sleep_guide")
    data object Development : Screen("development")
    data object CareGuide : Screen("care_guide")
    data object Subscription : Screen("subscription")
    
    // Novas telas do ecossistema do bebê
    data object Diary : Screen("diary")
    data object Documents : Screen("documents")
    data object History : Screen("history")
    data object Growth : Screen("growth")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
    
    // NOVAS TELAS - Expansão da Gestação (ADITIVAS)
    data object WeeklyChecklist : Screen("weekly_checklist")
    data object Timeline : Screen("timeline")
    data object PregnancyContent : Screen("pregnancy_content")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = remember { UserPreferencesManager(context) }
    val userData by preferencesManager.userData.collectAsState(initial = UserData())
    
    // Subscription Manager
    val subscriptionManager = remember { SubscriptionManager.getInstance(context) }
    val isPremium by subscriptionManager.isPremium.collectAsState()
    
    // Define a tela inicial baseado no onboarding
    val startDestination = if (userData.onboardingCompleted) {
        Screen.Home.route
    } else {
        Screen.Onboarding.route
    }
    
    // Função para navegar para conteúdo premium
    fun navigateToPremiumContent(destination: String) {
        if (isPremium) {
            navController.navigate(destination)
        } else {
            navController.navigate(Screen.Subscription.route)
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                isPremium = isPremium,
                onCategoryClick = { category ->
                    navController.navigate(Screen.Checklist.createRoute(category))
                },
                onSleepGuideClick = {
                    navigateToPremiumContent(Screen.SleepGuide.route)
                },
                onDevelopmentClick = {
                    navigateToPremiumContent(Screen.Development.route)
                },
                onCareGuideClick = {
                    navigateToPremiumContent(Screen.CareGuide.route)
                },
                onSubscriptionClick = {
                    navController.navigate(Screen.Subscription.route)
                },
                // Novos callbacks para o ecossistema do bebê
                onDiaryClick = {
                    navigateToPremiumContent(Screen.Diary.route)
                },
                onDocumentsClick = {
                    navigateToPremiumContent(Screen.Documents.route)
                },
                onHistoryClick = {
                    navigateToPremiumContent(Screen.History.route)
                },
                onGrowthClick = {
                    navigateToPremiumContent(Screen.Growth.route)
                },
                onSearchClick = {
                    navigateToPremiumContent(Screen.Search.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                // NOVOS CALLBACKS - Expansão da Gestação (ADITIVOS)
                onWeeklyChecklistClick = {
                    navController.navigate(Screen.WeeklyChecklist.route)
                },
                onTimelineClick = {
                    navController.navigate(Screen.Timeline.route)
                },
                onPregnancyContentClick = {
                    navController.navigate(Screen.PregnancyContent.route)
                }
            )
        }
        
        composable(
            route = Screen.Checklist.route,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category") ?: ""
            val category = try {
                ChecklistCategory.valueOf(categoryName)
            } catch (e: IllegalArgumentException) {
                ChecklistCategory.MATERNIDADE
            }
            
            ChecklistScreen(
                category = category,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.SleepGuide.route) {
            SleepGuideScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Development.route) {
            DevelopmentScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.CareGuide.route) {
            CareGuideScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Subscription.route) {
            SubscriptionScreen(
                subscriptionManager = subscriptionManager,
                onBackClick = { navController.popBackStack() },
                onSubscriptionSuccess = { navController.popBackStack() }
            )
        }
        
        // Novas rotas do ecossistema do bebê
        composable(Screen.Diary.route) {
            DiaryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Documents.route) {
            DocumentsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Growth.route) {
            GrowthScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // ============ NOVAS ROTAS - Expansão da Gestação (ADITIVAS) ============
        
        composable(Screen.WeeklyChecklist.route) {
            WeeklyChecklistScreen(
                onBackClick = { navController.popBackStack() },
                currentWeek = userData.currentWeek.takeIf { it > 0 } ?: 12
            )
        }
        
        composable(Screen.Timeline.route) {
            TimelineScreen(
                onBackClick = { navController.popBackStack() },
                currentWeek = userData.currentWeek.takeIf { it > 0 } ?: 20
            )
        }
        
        composable(Screen.PregnancyContent.route) {
            PregnancyContentScreen(
                onBackClick = { navController.popBackStack() },
                currentWeek = userData.currentWeek.takeIf { it > 0 } ?: 20
            )
        }
    }
}
