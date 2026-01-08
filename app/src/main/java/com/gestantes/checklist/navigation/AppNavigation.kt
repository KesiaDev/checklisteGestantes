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
import com.gestantes.checklist.ui.belly.BellyGalleryScreen
import com.gestantes.checklist.ui.letter.BabyLetterScreen
import com.gestantes.checklist.ui.shower.BabyShowerScreen
import com.gestantes.checklist.ui.contraction.ContractionScreen
import com.gestantes.checklist.ui.reminder.ReminderScreen
import com.gestantes.checklist.ui.notification.NotificationSettingsScreen
import com.gestantes.checklist.ui.adoption.AdoptionScreen
import com.gestantes.checklist.ui.tools.DueDateCalculatorScreen
import com.gestantes.checklist.ui.tools.KickCounterScreen
import com.gestantes.checklist.ui.tools.BabySizeScreen
import com.gestantes.checklist.ui.tools.BabyNamesScreen
import com.gestantes.checklist.ui.tools.BirthPlanScreen

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
    
    // NOVAS TELAS - Expansão v2.0
    data object BellyGallery : Screen("belly_gallery")
    data object BabyLetter : Screen("baby_letter")
    data object BabyShower : Screen("baby_shower")
    data object Contraction : Screen("contraction")
    data object Reminder : Screen("reminder")
    
    // Configurações de Notificação
    data object NotificationSettings : Screen("notification_settings")
    
    // Apoio à Adoção
    data object Adoption : Screen("adoption")
    
    // Ferramentas Essenciais
    data object DueDateCalculator : Screen("due_date_calculator")
    data object KickCounter : Screen("kick_counter")
    data object BabySize : Screen("baby_size")
    data object BabyNames : Screen("baby_names")
    data object BirthPlan : Screen("birth_plan")
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
                },
                // NOVOS CALLBACKS - Expansão v2.0
                onBellyGalleryClick = {
                    navController.navigate(Screen.BellyGallery.route)
                },
                onBabyLetterClick = {
                    navController.navigate(Screen.BabyLetter.route)
                },
                onBabyShowerClick = {
                    navController.navigate(Screen.BabyShower.route)
                },
                onContractionClick = {
                    navController.navigate(Screen.Contraction.route)
                },
                onReminderClick = {
                    navController.navigate(Screen.Reminder.route)
                },
                onNotificationSettingsClick = {
                    navController.navigate(Screen.NotificationSettings.route)
                },
                onAdoptionClick = {
                    navController.navigate(Screen.Adoption.route)
                },
                // Ferramentas Essenciais
                onDueDateCalculatorClick = {
                    navController.navigate(Screen.DueDateCalculator.route)
                },
                onKickCounterClick = {
                    navController.navigate(Screen.KickCounter.route)
                },
                onBabySizeClick = {
                    navController.navigate(Screen.BabySize.route)
                },
                onBabyNamesClick = {
                    navController.navigate(Screen.BabyNames.route)
                },
                onBirthPlanClick = {
                    navController.navigate(Screen.BirthPlan.route)
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
        
        // ============ NOVAS ROTAS - Expansão v2.0 ============
        
        composable(Screen.BellyGallery.route) {
            BellyGalleryScreen(
                onBackClick = { navController.popBackStack() },
                currentWeek = userData.currentWeek.takeIf { it > 0 } ?: 20
            )
        }
        
        composable(Screen.BabyLetter.route) {
            BabyLetterScreen(
                onBackClick = { navController.popBackStack() },
                currentWeek = userData.currentWeek.takeIf { it > 0 } ?: 20
            )
        }
        
        composable(Screen.BabyShower.route) {
            BabyShowerScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Contraction.route) {
            ContractionScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Reminder.route) {
            ReminderScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Configurações de Notificação
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Apoio à Adoção
        composable(Screen.Adoption.route) {
            AdoptionScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // ============ FERRAMENTAS ESSENCIAIS ============
        
        composable(Screen.DueDateCalculator.route) {
            DueDateCalculatorScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.KickCounter.route) {
            KickCounterScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.BabySize.route) {
            BabySizeScreen(
                onBackClick = { navController.popBackStack() },
                currentWeek = userData.currentWeek.takeIf { it > 0 } ?: 20
            )
        }
        
        composable(Screen.BabyNames.route) {
            BabyNamesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.BirthPlan.route) {
            BirthPlanScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
