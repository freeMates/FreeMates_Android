package com.example.freemates_android

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

private val ACCESS_TOKEN = stringPreferencesKey("access_token")
private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

object TokenManager {

    /* 1) 저장 -------------------------------------------------------------- */
    suspend fun Context.saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        authDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN]  = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
        }
    }

    /* 2) 조회  ------------------------------------------------------------- */
    suspend fun Context.getAccessToken(): String =
        authDataStore.data.map { it[ACCESS_TOKEN] ?: "" }.first()

    suspend fun Context.getRefreshToken(): String =
        authDataStore.data.map { it[REFRESH_TOKEN] ?: "" }.first()
}