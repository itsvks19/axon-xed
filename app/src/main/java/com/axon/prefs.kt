package com.axon

import com.rk.settings.Preference

const val AXON_API_KEY = "axon_api_key"
val apiKey get() = Preference.getString(AXON_API_KEY, "")

var lastLLMProvider: String = ""
    get() = Preference.getString("last_llm_provider", field)
    set(value) = Preference.setString("last_llm_provider", value)

var lastLLModel: String = ""
    get() = Preference.getString("last_ll_model", field)
    set(value) = Preference.setString("last_ll_model", value)

