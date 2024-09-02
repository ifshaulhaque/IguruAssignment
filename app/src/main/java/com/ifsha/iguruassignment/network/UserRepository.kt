package com.ifsha.iguruassignment.network

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ifsha.iguruassignment.db.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.stream.Collectors

class UserRepository(private val apiService: ApiService, private val userDao: UserDao) {

    fun fetchUsers(): LiveData<List<User>> {
        val data = MutableLiveData<List<User>>()
        
        // Fetch from API
        apiService.getUsers().enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { userList ->
                        data.postValue(userList)
                        // Save to local database
                        CoroutineScope(Dispatchers.IO).launch {
                            userDao.insertAll(userList.stream().map { com.ifsha.iguruassignment.db.User(
                                name = "${it.first_name} ${it.last_name}",
                                email = it.email,
                                avatar = it.avatar,
                                id = it.id
                            ) }.collect(Collectors.toList()))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                // In case of error, load from local database
//                CoroutineScope(Dispatchers.IO).launch {
//                    data.postValue(userDao.getUsers().value)
//                }
            }
        })

        return data
    }

    fun getUsersFromDb(): MutableLiveData<List<User>> {
        return MutableLiveData(
            userDao.getUsers().value?.stream()?.map {
                User(
                    id = it.id,
                    first_name = it.name,
                    avatar = it.avatar,
                    email = it.email,
                    last_name = ""
                )
            }?.collect(Collectors.toList())
        )
    }
}
