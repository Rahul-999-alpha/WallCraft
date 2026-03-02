package com.rahul.clearwalls.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.core.util.AdConfig
import com.rahul.clearwalls.core.util.AdminConfigManager
import com.rahul.clearwalls.core.util.RefreshConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminConfigManager: AdminConfigManager
) : ViewModel() {

    val adConfig: StateFlow<AdConfig> = adminConfigManager.adConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdConfig())

    val refreshConfig: StateFlow<RefreshConfig> = adminConfigManager.refreshConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RefreshConfig())

    val isAuthenticated: StateFlow<Boolean> = adminConfigManager.isAdminAuthenticated
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val premiumEnabled: StateFlow<Boolean> = adminConfigManager.premiumEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    fun authenticate(password: String) {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }

        if (hash == Constants.ADMIN_PASSWORD_HASH) {
            viewModelScope.launch {
                adminConfigManager.setAdminAuthenticated(true)
            }
            _authError.value = null
        } else {
            _authError.value = "Incorrect password"
        }
    }

    fun updateAdConfig(config: AdConfig) {
        viewModelScope.launch { adminConfigManager.updateAdConfig(config) }
    }

    fun updateRefreshConfig(config: RefreshConfig) {
        viewModelScope.launch { adminConfigManager.updateRefreshConfig(config) }
    }

    fun setPremiumEnabled(enabled: Boolean) {
        viewModelScope.launch { adminConfigManager.setPremiumEnabled(enabled) }
    }

    fun logout() {
        viewModelScope.launch { adminConfigManager.setAdminAuthenticated(false) }
    }
}
