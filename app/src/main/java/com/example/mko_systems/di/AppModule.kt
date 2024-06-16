package com.example.mko_systems.di

import android.content.Context
import androidx.room.Room
import com.example.mko_systems.data.UserDao
import com.example.mko_systems.data.UserDatabase
import com.example.mko_systems.data.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): UserDatabase {
        return Room.databaseBuilder(
            appContext,
            UserDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }
}