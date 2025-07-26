package com.axon

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Bot
import com.composables.icons.lucide.Lucide
import com.rk.controlpanel.ControlItem
import com.rk.extension.Extension
import com.rk.extension.ExtensionAPI
import com.rk.extension.SettingsScreen
import com.rk.pluginApi.PluginApi
import com.rk.xededitor.MainActivity.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val id = "axon"
const val tabName = "Axon"

class Axon : ExtensionAPI() {
    private val scope = MainScope()

    override fun onPluginLoaded(extension: Extension) {
        Log.println(Log.INFO, "Axon", "Plugin Loaded")

        PluginApi.registerTab(id, tabName) { AxonFragment() }
        PluginApi.registerSettingsScreen(
            id = id,
            screen = SettingsScreen(
                label = "Axon",
                description = "Configure Axon",
                route = "${id}_settings",
                icon = {
                    Icon(
                        Lucide.Bot,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            ) {
                com.axon.ui.SettingsScreen(modifier = Modifier.fillMaxSize())
            }
        )

        PluginApi.registerControlItem(
            id = id,
            item = ControlItem(
                label = tabName,
                description = "Open Axon AI",
                hideControlPanelOnClick = true,
                keyBind = "Ctrl+Alt+A",
                sideEffect = { PluginApi.openRegisteredTab(id = id, tabName = tabName) }
            )
        )

        scope.launch {
            MainActivity.activityRef.get()?.menu?.apply {
                val browserItem = add("Axon AI")
                browserItem.setOnMenuItemClickListener {
                    PluginApi.openRegisteredTab(id = id, tabName = tabName)
                    true
                }
            }
        }
    }

    override fun onMainActivityPaused() {}
    override fun onMainActivityResumed() {}
    override fun onMainActivityDestroyed() {}
    override fun onLowMemory() {}
}
