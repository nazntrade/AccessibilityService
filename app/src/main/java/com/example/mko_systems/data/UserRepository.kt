package com.example.mko_systems.data

import com.example.mko_systems.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUser(): User? {
        return userDao.getUser()
    }
}