package com.example.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.ui.screens.*
import com.example.utils.ThemePreferences
import com.example.viewmodel.MaskArisanViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: MaskArisanViewModel, themePreferences: ThemePreferences) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        Pair(DashboardRoute, Icons.Default.Home to "Dashboard"),
        Pair(PesertaRoute, Icons.Default.Person to "Peserta"),
        Pair(SetoranRoute, Icons.Default.List to "Setoran"),
        Pair(ArisanRoute, Icons.Default.PlayArrow to "Arisan")
    )

    val drawerItems = listOf(
        Pair(BelumSetorRoute, Icons.Default.Warning to "Belum Setor"),
        Pair(RiwayatRoute, Icons.Default.History to "Riwayat"),
        Pair(StatistikRoute, Icons.Default.BarChart to "Statistik"),
        Pair(PengaturanRoute, Icons.Default.Settings to "Pengaturan"),
        Pair(TentangRoute, Icons.Default.Info to "Tentang")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu Lainnya", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                Divider()
                drawerItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.first::class.qualifiedName } == true
                    NavigationDrawerItem(
                        icon = { Icon(item.second.first, contentDescription = null) },
                        label = { Text(item.second.second) },
                        selected = isSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.first) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("MASKARISAN", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1E1E1E), letterSpacing = 0.5.sp)
                            Text("Maskaav v1.0.0", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E1E1E).copy(alpha = 0.7f))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color(0xFF1E1E1E),
                        navigationIconContentColor = Color(0xFF1E1E1E)
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                // Determine if bottom bar should be shown
                val showBottomBar = bottomNavItems.any { it.first::class.qualifiedName == currentDestination?.route }
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Color(0xFF64748B),
                        tonalElevation = 8.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentDestination?.hierarchy?.any { it.route == item.first::class.qualifiedName } == true
                            NavigationBarItem(
                                icon = { Icon(item.second.first, contentDescription = null) },
                                label = { Text(item.second.second, fontSize = 10.sp, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium) },
                                selected = isSelected,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    unselectedIconColor = Color(0xFF94A3B8),
                                    unselectedTextColor = Color(0xFF64748B)
                                ),
                                onClick = {
                                    navController.navigate(item.first) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = DashboardRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<DashboardRoute> { DashboardScreen(viewModel) }
                composable<PesertaRoute> { PesertaScreen(viewModel) }
                composable<SetoranRoute> { SetoranScreen(viewModel) }
                composable<BelumSetorRoute> { BelumSetorScreen(viewModel) }
                composable<ArisanRoute> { ArisanScreen(viewModel) }
                composable<RiwayatRoute> { RiwayatScreen(viewModel) }
                composable<StatistikRoute> { StatistikScreen(viewModel) }
                composable<PengaturanRoute> { PengaturanScreen(viewModel, themePreferences) }
                composable<TentangRoute> { TentangScreen() }
            }
        }
    }
}
