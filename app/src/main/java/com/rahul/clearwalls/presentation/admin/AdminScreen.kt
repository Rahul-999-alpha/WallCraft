package com.rahul.clearwalls.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onBackClick: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val authError by viewModel.authError.collectAsState()
    val adConfig by viewModel.adConfig.collectAsState()
    val refreshConfig by viewModel.refreshConfig.collectAsState()
    val premiumEnabled by viewModel.premiumEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isAuthenticated) {
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(Icons.AutoMirrored.Filled.Logout, "Logout")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (!isAuthenticated) {
            // Password screen
            var password by remember { mutableStateOf("") }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Enter Admin Password", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = authError != null,
                    supportingText = authError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.authenticate(password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
            }
        } else {
            // Admin controls
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Ad Controls
                SectionTitle("Ad Controls")
                AdminToggle("Banner Ads", adConfig.bannerEnabled) {
                    viewModel.updateAdConfig(adConfig.copy(bannerEnabled = it))
                }
                AdminToggle("Native Ads", adConfig.nativeEnabled) {
                    viewModel.updateAdConfig(adConfig.copy(nativeEnabled = it))
                }
                AdminToggle("Interstitial Ads", adConfig.interstitialEnabled) {
                    viewModel.updateAdConfig(adConfig.copy(interstitialEnabled = it))
                }
                AdminToggle("Rewarded Ads", adConfig.rewardedEnabled) {
                    viewModel.updateAdConfig(adConfig.copy(rewardedEnabled = it))
                }

                Text(
                    "Native Ad Interval: every ${adConfig.nativeAdInterval} items",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                Slider(
                    value = adConfig.nativeAdInterval.toFloat(),
                    onValueChange = {
                        viewModel.updateAdConfig(adConfig.copy(nativeAdInterval = it.toInt()))
                    },
                    valueRange = 4f..30f,
                    steps = 25,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Refresh Controls
                SectionTitle("Refresh Controls")
                AdminToggle("Auto Refresh", refreshConfig.autoRefreshEnabled) {
                    viewModel.updateRefreshConfig(refreshConfig.copy(autoRefreshEnabled = it))
                }
                Text(
                    "Refresh Interval: ${refreshConfig.refreshIntervalHours}h",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                Slider(
                    value = refreshConfig.refreshIntervalHours.toFloat(),
                    onValueChange = {
                        viewModel.updateRefreshConfig(refreshConfig.copy(refreshIntervalHours = it.toLong()))
                    },
                    valueRange = 1f..24f,
                    steps = 22,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Premium Toggle
                SectionTitle("Premium Features")
                AdminToggle("Enable Premium (Testing)", premiumEnabled) {
                    viewModel.setPremiumEnabled(it)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun AdminToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
