package com.bekalmu.pos.ui.pengaturan

import androidx.lifecycle.*
import com.bekalmu.pos.data.model.User
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.utils.HashUtils
import kotlinx.coroutines.launch

class PengaturanViewModel(private val repository: AppRepository) : ViewModel() {

    val allUsers = repository.getAllUsers()

    private val _operationResult = MutableLiveData<String?>()
    val operationResult: LiveData<String?> = _operationResult

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _currentUser.postValue(repository.getUserById(userId))
        }
    }

    fun addStaf(nama: String, username: String, password: String, role: String) {
        if (nama.isBlank() || username.isBlank() || password.isBlank()) {
            _operationResult.value = "Semua field wajib diisi"
            return
        }
        viewModelScope.launch {
            try {
                repository.insertUser(
                    User(nama = nama, username = username, password = HashUtils.hash(password), role = role)
                )
                _operationResult.value = "Staf berhasil ditambahkan"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                repository.updateUser(user)
                _operationResult.value = "Profil berhasil diperbarui"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                repository.deleteUser(user)
                _operationResult.value = "Staf berhasil dihapus"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun clearResult() { _operationResult.value = null }
}
