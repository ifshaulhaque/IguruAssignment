package com.ifsha.iguruassignment.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {
    companion object {
        var instance: Retrofit? = null

        fun getApi(): Retrofit {
            if (instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl("https://reqres.in/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return instance!!
        }
    }
}