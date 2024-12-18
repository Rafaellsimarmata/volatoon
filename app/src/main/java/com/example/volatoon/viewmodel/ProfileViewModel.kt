package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.ProfileResponse
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class ProfileViewModel: ViewModel() {
    private val _profileResState = mutableStateOf(ProfileResState())
    val profileResState : State<ProfileResState> = _profileResState

    private val _userData = mutableStateOf<CurrentUserData?>(null)
    val userData: State<CurrentUserData?> = _userData

    fun fetchUserData(dataStoreManager : DataStoreManager) {
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val bearerToken = "Bearer $token"
                    val response = apiService.getProfile(bearerToken)

                    _profileResState.value = _profileResState.value.copy(
                        loading = false,
                        profileDataRes = response,
                        error = null
                    )

                    val userData = response.body()?.userData.let {
                        CurrentUserData(
                            fullName = it?.fullName ?: "",
                            userName = it?.userName ?: "",
                            email = it?.email ?: "",
                            status = it?.status ?: "",
                            ispremium = it?.ispremium ?: false
                        )
                    }
                    _userData.value = userData

                } catch (e : Exception){
                    _profileResState.value = _profileResState.value.copy(
                        loading = false,
                        error = "error fetching profile ${e.message}"
                    )
                }
            }
        }
    }

    fun updateStatus(newStatus: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken

            if (token != null) {
                try {
                    val bearerToken = "Bearer $token"
                    val response = apiService.updateUserStatus(bearerToken, newStatus)

                    if (response.isSuccessful) {
                        fetchUserData(dataStoreManager)
                    } else {
                        _profileResState.value = _profileResState.value.copy(
                            error = "Error updating status: ${response.message()}"
                        )
                    }
                } catch (e: Exception) {
                    _profileResState.value = _profileResState.value.copy(
                        error = "Error updating status: ${e.message}"
                    )
                }
            }
        }
    }

    data class ProfileResState(
        val loading : Boolean = true,
        val profileDataRes : Response<ProfileResponse>? = null,
        val error : String? = null
    )

    data class CurrentUserData(
        val fullName : String?,
        val userName : String?,
        val email : String?,
        val status : String?,
        val ispremium: Boolean?
    )
}