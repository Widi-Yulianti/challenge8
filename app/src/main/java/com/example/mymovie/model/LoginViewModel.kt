package com.example.mymovie.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovie.datastore.DataStoreManager
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: DataStoreManager): ViewModel() {
    fun getUser(): LiveData<UserPreferences> {
        return pref.getUser().asLiveData()
    }

    fun setUserLogin(isLogin: Boolean) {
        viewModelScope.launch {
            pref.setUserLogin(isLogin)
        }
    }

    fun saveUser(id: String,
                 name: String,
                 username: String,
                 password: String,
                 age: String,
                 address: String) {
        viewModelScope.launch {
            pref.setUser(id, name, username, password, age, address)
        }
    }
}