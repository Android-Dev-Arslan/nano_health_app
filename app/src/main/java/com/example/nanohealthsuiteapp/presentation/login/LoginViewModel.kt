package com.example.nanohealthsuiteapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.data.remote.dto.auth.LoginResponse
import com.example.nanohealthsuiteapp.domain.use_cases.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _userLoginFlow = MutableSharedFlow<Resource<LoginResponse?>>()
    val userLoginFlow = _userLoginFlow.asSharedFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(username, password).collect {
                _userLoginFlow.emit(it)
            }
        }
    }
}