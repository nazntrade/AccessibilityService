package com.example.mko_systems.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val id: Int = 1,
    val name: String
)