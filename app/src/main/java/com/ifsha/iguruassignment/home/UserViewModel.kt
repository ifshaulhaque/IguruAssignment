package com.ifsha.iguruassignment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifsha.iguruassignment.network.User
import com.ifsha.iguruassignment.network.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val users: MutableLiveData<List<User>> = repository.getUsersFromDb()

    fun fetchUsers(): LiveData<List<User>> {
        return repository.fetchUsers()
    }
}
