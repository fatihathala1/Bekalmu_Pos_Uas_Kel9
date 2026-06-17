package com.bekalmu.pos.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekalmu.pos.data.model.User
import com.bekalmu.pos.data.repository.AppRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(username: String, password: String) {
        if (username.isBlank()) {
            _loginResult.value = LoginResult.Error("Pilih kasir terlebih dahulu")
            return
        }
        if (password.isBlank()) {
            _loginResult.value = LoginResult.Error("Password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = repository.login(username.trim(), password.trim())
                if (user != null && user.isActive) {
                    _loginResult.value = LoginResult.Success(user)
                } else {
                    _loginResult.value = LoginResult.Error("Username atau password salah")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Terjadi kesalahan: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    sealed class LoginResult {
        data class Success(val user: User) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }
}
