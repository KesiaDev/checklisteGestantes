package com.gestantes.checklist.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.gestantes.checklist.billing.SubscriptionManager
import com.gestantes.checklist.data.entity.ChecklistCategory
import com.gestantes.checklist.data.preferences.UserData
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import com.gestantes.checklist.ui.checklist.ChecklistScreen
import com.gestantes.checklist.ui.components.AppDrawer
import com.gestantes.checklist.ui.diary.DiaryScreen
import com.gestantes.checklist.ui.documents.DocumentsScreen
import com.gestantes.checklist.ui.growth.GrowthScreen
import com.gestantes.checklist.ui.guides.CareGuideScreen
import com.gestantes.checklist.ui.guides.DevelopmentScreen
import com.gestantes.checklist.ui.guides.SleepGuideScreen
import com.gestantes.checklist.ui.history.HistoryScreen
import com.gestantes.checklist.ui.home.DashboardScreen
import com.gestantes.checklist.ui.onboarding.OnboardingScreen
import com.gestantes.checklist.ui.search.SearchScreen
import com.gestantes.checklist.ui.sections.*
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
import kotlinx.coroutines.launch

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
    
    // NOVAS TELAS DE SEÇÃO (Menu organizado)
    data object SectionPregnancy : Screen("section_pregnancy")
    data object SectionTools : Screen("section_tools")
    data object SectionBaby : Screen("section_baby")
    data object SectionChecklists : Screen("section_checklists")
    data object SectionGuides : Screen("section_guides")
    data object SectionSpecial : Screen("section_special")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = remember { UserPreferencesManager(context) }
    val userData by preferencesManager.userData.collectAsState(initial = UserData())
    
    // Subscription Manager
    val subscriptionManager = remember { SubscriptionManager.getInstance(context) }
    val isPremium by subscriptionManager.isPremium.collectAsState()
    
    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""
    
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
    
    // Função de navegação unificada
    fun handleNavigation(route: String) {
        when {
            // Rotas que precisam de premium
            route in listOf("diary", "documents", "history", "growth", "search", 
                           "sleep_guide", "development", "care_guide") -> {
                navigateToPremiumContent(route)
            }
            // Rota de checklist com categoria
            route.startsWith("checklist/") -> {
                navController.navigate(route)
            }
            // Outras rotas
            else -> {
                navController.navigate(route)
            }
        }
    }
    
    // Telas que devem mostrar o drawer (tela principal)
    val showDrawer = currentRoute == Screen.Home.route
    
    // Wrapper com Drawer para a tela Home
    if (showDrawer) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    momName = userData.momName,
                    currentWeek = userData.currentWeek.takeIf { it > 0 } ?: 20,
                    isPremium = isPremium,
                    onNavigate = { route ->
                        scope.launch { drawerState.close() }
                        handleNavigation(route)
                    },
                    onClose = { scope.launch { drawerState.close() } }
                )
            }
        ) {
            AppNavigationContent(
                navController = navController,
                startDestination = startDestination,
                userData = userData,
                isPremium = isPremium,
                onOpenDrawer = { scope.launch { drawerState.open() } },
                onNavigate = ::handleNavigation
            )
        }
    } else {
        AppNavigationContent(
            navController = navController,
            startDestination = startDestination,
            userData = userData,
            isPremium = isPremium,
            onOpenDrawer = { },
            onNavigate = ::handleNavigation
        )
    }
}

@Composable
private fun AppNavigationContent(
    navController: NavHostController,
    startDestination: String,
    userData: UserData,
    isPremium: Boolean,
    onOpenDrawer: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val currentWeek = remember(userData.currentWeek) { userData.currentWeek.takeIf { it > 0 } ?: 20 }
    
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
        
        // NOVA HOME - Dashboard com Drawer
        composable(Screen.Home.route) {
            DashboardScreen(
                userData = userData,
                isPremium = isPremium,
                onOpenDrawer = onOpenDrawer,
                onNavigate = onNavigate
            )
        }
        
        // ============ NOVAS TELAS DE SEÇÃO ============
        
        composable(Screen.SectionPregnancy.route) {
            PregnancySectionScreen(
                currentWeek = currentWeek,
                isPremium = isPremium,
                onBackClick = { navController.popBackStack() },
                onNavigate = onNavigate
            )
        }
        
        composable(Screen.SectionTools.route) {
            ToolsSectionScreen(
                isPremium = isPremium,
                onBackClick = { navController.popBackStack() },
                onNavigate = onNavigate
            )
        }
        
        composable(Screen.SectionBaby.route) {
            BabySectionScreen(
                isPremium = isPremium,
                onBackClick = { navController.popBackStack() },
                onNavigate = onNavigate
            )
        }
        
        composable(Screen.SectionChecklists.route) {
            ChecklistsSectionScreen(
                isPremium = isPremium,
                onBackClick = { navController.popBackStack() },
                onNavigate = onNavigate
            )
        }
        
        composable(Screen.SectionGuides.route) {
            GuidesSectionScreen(
                isPremium = isPremium,
                onBackClick = { navController.popBackStack() },
                onNavigate = onNavigate
            )
        }
        
        composable(Screen.SectionSpecial.route) {
            SpecialSectionScreen(
                isPremium = isPremium,
                onBackClick = { navController.popBackStack() },
                onNavigate = onNavigate
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
