package com.example.mko_systems.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mko_systems.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _accountName = MutableLiveData<String>()
    val accountName: LiveData<String>
        get() = _accountName

    fun fetchAccountName() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUser()
            user?.let { _accountName.postValue(it.name) }
        }
    }
}