package com.example.mymovie.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mymovie.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager (private val context: Context) {

    suspend fun setUser(id: String,
                        name: String,
                        username: String,
                        password: String,
                        age: String,
                        address: String) {
        context.loginDataStore.edit { preferences ->
            preferences[ID_KEY] = id
            preferences[NAME_KEY] = name
            preferences[USERNAME_KEY] = username
            preferences[PASSWORD_KEY] = password
            preferences[AGE_KEY] = age
            preferences[ADDRESS_KEY] = address
        }
    }

    suspend fun setUserLogin(isLogin: Boolean) {
        context.loginDataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = isLogin
        }
    }

    fun getUser(): Flow<UserPreferences> {
        return context.loginDataStore.data.map { preferences ->
            UserPreferences(
                preferences[ID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[USERNAME_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[AGE_KEY] ?: "",
                preferences[ADDRESS_KEY] ?: ""
            )
        }
    }

    fun getUserLogin(): Flow<Boolean> {
        return context.loginDataStore.data.map { preferences ->
            preferences[LOGIN_STATUS_KEY] ?: false
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "login_preferences"
        private val ID_KEY = stringPreferencesKey("id_key")
        private val NAME_KEY = stringPreferencesKey("name_key")
        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val PASSWORD_KEY = stringPreferencesKey("password_key")
        private val AGE_KEY = stringPreferencesKey("age_key")
        private val ADDRESS_KEY = stringPreferencesKey("address_key")
        private val LOGIN_STATUS_KEY = booleanPreferencesKey("login_status_key")

        val Context.loginDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    }
}