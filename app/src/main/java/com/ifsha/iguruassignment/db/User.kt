package com.ifsha.iguruassignment.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val avatar: String,
    val uploadedImageUrl: String? = null
)
