package com.axon

import android.content.Context
import android.provider.Settings

fun Context.isImeGBoard(): Boolean {
    val im = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
    return im.contains("com.google.android.inputmethod", ignoreCase = true)
}
