package com.ifsha.iguruassignment.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users?page=2")
    fun getUsers(): Call<UserListResponse>
}
