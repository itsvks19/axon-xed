package com.axon.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.axon.AXON_API_KEY
import com.rk.components.compose.preferences.base.PreferenceGroup
import com.rk.components.compose.preferences.base.PreferenceLayout
import com.rk.settings.Preference
import com.rk.xededitor.ui.components.EditorSettingsToggle
import com.rk.xededitor.ui.components.InputDialog

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    PreferenceLayout(
        modifier = modifier,
        label = "Axon",
        backArrowVisible = true
    ) {
        var showApiKeyDialog by remember { mutableStateOf(false) }
        var apiKey by remember { mutableStateOf(Preference.getString(AXON_API_KEY, "")) }

        PreferenceGroup(heading = "API Settings") {
            EditorSettingsToggle(
                label = "API Key",
                description = "Set your API Key to use Axon AI.",
                showSwitch = false,
                default = false,
                sideEffect = { showApiKeyDialog = true }
            )
        }

        if (showApiKeyDialog) {
            InputDialog(
                title = "API Key",
                inputLabel = "Enter your API Key",
                inputValue = apiKey,
                onInputValueChange = { apiKey = it },
                onConfirm = {
                    Preference.setString(AXON_API_KEY, apiKey)
                    showApiKeyDialog = false
                },
                onDismiss = { showApiKeyDialog = false },
                singleLineMode = true
            )
        }
    }
}
