package com.axon

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.axon.ui.Axon
import com.rk.xededitor.MainActivity.MainActivity
import com.rk.xededitor.MainActivity.tabs.core.CoreFragment

class AxonFragment : CoreFragment() {
    override fun getView() = ComposeView(requireNotNull(MainActivity.activityRef.get())).apply {
        Log.println(Log.INFO, "Axon", "ComposeView")
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            Axon(modifier = Modifier.fillMaxSize())
        }
    }

    override fun onDestroy() {}
    override fun onCreate() {}
    override fun onClosed() {}
}
