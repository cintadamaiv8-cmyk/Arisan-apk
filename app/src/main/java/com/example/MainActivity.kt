package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.data.database.AppDatabase
import com.example.data.repository.AppRepository
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.AppTheme
import com.example.utils.ThemePreferences
import com.example.viewmodel.MaskArisanViewModel
import com.example.viewmodel.MaskArisanViewModelFactory
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private lateinit var themePreferences: ThemePreferences
    private lateinit var viewModel: MaskArisanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val repository = AppRepository(
            database.pesertaDao(),
            database.setoranDao(),
            database.riwayatDao(),
            database.auditLogDao(),
            database.pengaturanDao()
        )
        viewModel = ViewModelProvider(this, MaskArisanViewModelFactory(repository))[MaskArisanViewModel::class.java]
        themePreferences = ThemePreferences(this)

        setContent {
            val themeMode by themePreferences.themeMode.collectAsState(initial = "system")
            var showSplash by remember { mutableStateOf(true) }

            AppTheme(themeMode = themeMode) {
                if (showSplash) {
                    SplashScreen(onFinish = { showSplash = false })
                } else {
                    AppNavigation(viewModel, themePreferences)
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    var scaled by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(targetValue = if (scaled) 1f else 0.5f, animationSpec = tween(1000), label = "scale")

    LaunchedEffect(Unit) {
        scaled = true
        for (i in 1..100) {
            progress = i / 100f
            delay(30)
        }
        onFinish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4B400)), // App Theme Primary
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "M",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.scale(scaleAnim)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("MASKARISAN", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(32.dp))
        Text("${(progress * 100).toInt()}%", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Didukung oleh Maskaav", fontSize = 12.sp, color = Color.White)
    }
}
