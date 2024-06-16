package com.example.mko_systems.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mko_systems.data.model.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}