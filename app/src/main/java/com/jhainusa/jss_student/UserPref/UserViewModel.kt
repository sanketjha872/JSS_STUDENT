package com.jhainusa.jss_student.UserPref

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NameViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _nameFlow = MutableStateFlow<String?>(null)
    val nameFlow: StateFlow<String?> = _nameFlow.asStateFlow()

    init {
        viewModelScope.launch {
            UserPreferences.getName(context).collect {
                _nameFlow.value = it
            }
        }
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            UserPreferences.saveName(context, name)
        }
    }
}
