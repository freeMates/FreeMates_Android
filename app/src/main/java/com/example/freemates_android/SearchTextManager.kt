package com.example.freemates_android

import android.content.Context
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.freemates_android.model.SearchItem
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.searchPrefs by preferencesDataStore(name = "search_prefs")

private val RECENT_SEARCH_TOKEN = stringSetPreferencesKey("recent_search_token")
private const val MAX_RECENT = 10

//// ③ 저장 추가
//suspend fun Context.addRecentSearch(query: String) {
//    searchPrefs.edit { prefs ->
//        val current = prefs[RECENT_SEARCH_TOKEN]?.toMutableList() ?: mutableListOf()
//        current.remove(query)
//        current.add(query)
//        prefs[RECENT_SEARCH_TOKEN] = current.takeLast(MAX_RECENT).toSet()
//    }
//}
//
//// ④ 조회 Flow
//fun Context.recentSearches(): List<String> =
//    searchPrefs.data.map { prefs ->
//        val unordered = prefs[RECENT_SEARCH_TOKEN]?.toList() ?: emptyList()
//        // LinkedHashSet으로 다시 정렬(최신순)
//        val ordered = linkedSetOf<String>().apply { addAll(unordered) }.toList()
//        ordered.asReversed()  // 최신 → 과거
//    }

private const val PREF_NAME = "my_pref"
private const val KEY_LIST = "string_list"

fun Context.saveStringList(list: ArrayList<String>) {
    val json = Gson().toJson(list)
    getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_LIST, json)
        .apply()          // apply() 비동기, commit() 동기
}

fun Context.loadStringList(): ArrayList<String> {
    val json = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .getString(KEY_LIST, null) ?: return arrayListOf()

    val type = object : TypeToken<ArrayList<String>>() {}.type
    return Gson().fromJson(json, type)
}