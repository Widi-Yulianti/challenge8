package com.example.mymovie.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovie.data.DataUser
import com.example.mymovie.network.RestfulUser
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ViewModelUser @Inject constructor(val api: RestfulUser) : ViewModel() {
    var getLdUser : MutableLiveData<List<ResponseUserItem>?> = MutableLiveData()
    var postLDUser: MutableLiveData<ResponseUserItem?> = MutableLiveData()
    var updLDUser: MutableLiveData<ResponseUserItem>
    var getUserById : MutableLiveData<ResponseUserItem?> = MutableLiveData()


    init {
        updLDUser = MutableLiveData()
    }

    fun postLiveDataUser(): MutableLiveData<ResponseUserItem?> {
        return postLDUser
    }

    fun updateLiveDataUser(): MutableLiveData<ResponseUserItem> {
        return updLDUser
    }

    fun getLiveDataUser(): MutableLiveData<List<ResponseUserItem>?> {
        return getLdUser
    }

    fun getLiveDataUserById() : MutableLiveData<ResponseUserItem?> {
        return getUserById
    }

    fun callGetUser() {
        api.getAllUser()
            .enqueue(object  : Callback<List<ResponseUserItem>> {
                override fun onResponse(
                    call: Call<List<ResponseUserItem>>,
                    response: Response<List<ResponseUserItem>>
                ) {
                    if (response.isSuccessful) {
                        getLdUser.postValue(response.body())
                    } else {
                        getLdUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                    getLdUser.postValue(null)
                }

            })
    }

    fun callPostUser(name: String,
                     username: String,
                     password: String,
                     age: String,
                     address: String) {
        api.postUser(DataUser(name, username, password,age, address))
            .enqueue(object : Callback<ResponseUserItem> {
                override fun onResponse(
                    call: Call<ResponseUserItem>,
                    response: Response<ResponseUserItem>
                ) {
                    if (response.isSuccessful) {
                        postLDUser.postValue(response.body())
                    } else {
                        postLDUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseUserItem>, t: Throwable) {
                    postLDUser.postValue(null)
                }

            })
    }

    fun updateApiUser(
        id: String,
        name: String,
        username: String,
        password: String,
        age: String,
        address: String
    ) {
        api.updateUser(id, DataUser(name, username, password, age, address))
            .enqueue(object : Callback<ResponseUserItem> {
                override fun onResponse(
                    call: Call<ResponseUserItem>,
                    response: Response<ResponseUserItem>
                ) {
                    if (response.isSuccessful) {
                        updLDUser.postValue(response.body())
                    } else {
                        updLDUser.postValue(null!!)
                    }
                }

                override fun onFailure(call: Call<ResponseUserItem>, t: Throwable) {
                    updLDUser.postValue(null!!)
                }

            })
    }

    fun getUserById(id: String) {
        api.getUserById(id)
            .enqueue(object : Callback<ResponseUserItem> {
                override fun onResponse(
                    call: Call<ResponseUserItem>,
                    response: Response<ResponseUserItem>
                ) {
                    if (response.isSuccessful) {
                        getUserById.postValue(response.body())
                    } else {
                        getUserById.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseUserItem>, t: Throwable) {
                    getUserById.postValue(null)
                }

            })
    }
}