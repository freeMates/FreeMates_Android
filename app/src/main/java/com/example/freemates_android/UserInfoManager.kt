package com.example.freemates_android

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "user_info")

private val USERNAME = stringPreferencesKey("username_info")
private val PASSWORD = stringPreferencesKey("password_info")
private val NICKNAME = stringPreferencesKey("nickname_info")
private val EMAIL = stringPreferencesKey("email_info")
private val AGE = stringPreferencesKey("age_info")
private val GENDER = stringPreferencesKey("gender_info")

object UserInfoManager {
    /* 1) 저장 -------------------------------------------------------------- */
    suspend fun Context.saveInfo(
        userName: String,
        password: String,
        nickname: String,
        email: String,
        age: String,
        gender: String,
    ) {
        authDataStore.edit { prefs ->
            prefs[USERNAME]  = userName
            prefs[PASSWORD]  = password
            prefs[NICKNAME] = nickname
            prefs[EMAIL]  = email
            prefs[AGE] = age
            prefs[GENDER]  = gender
        }
    }

    /* 2) 조회  ------------------------------------------------------------- */
    suspend fun Context.getUserNameInfo(): String =
        authDataStore.data.map { it[USERNAME] ?: "" }.first()

    suspend fun Context.getPasswordInfo(): String =
        authDataStore.data.map { it[PASSWORD] ?: "" }.first()

    suspend fun Context.getNicknameInfo(): String =
        authDataStore.data.map { it[NICKNAME] ?: "" }.first()

    suspend fun Context.getEmailInfo(): String =
        authDataStore.data.map { it[EMAIL] ?: "" }.first()

    suspend fun Context.getAgeInfo(): String =
        authDataStore.data.map { it[AGE] ?: "" }.first()

    suspend fun Context.getGenderInfo(): String =
        authDataStore.data.map { it[GENDER] ?: "" }.first()
}