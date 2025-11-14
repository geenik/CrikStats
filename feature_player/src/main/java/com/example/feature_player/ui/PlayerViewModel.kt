package com.example.feature_player.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crikstats.data.DataState
import com.example.crikstats.data.PlayerResponse
import com.example.crikstats.repository.GetDataRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class PlayerViewModel(
    val playerRepository: GetDataRepo
) : ViewModel() {

    private val _data = MutableStateFlow<DataState<PlayerResponse>>(DataState.Loading)
    val data = _data.asStateFlow()

    fun getData() = viewModelScope.launch {
        _data.value = DataState.Loading

        runCatching { playerRepository.getData() }
            .onSuccess { response ->
                if (response.isSuccessful && response.body() != null) {
                    _data.value = DataState.Success(response.body()!!)
                } else {
                    _data.value = DataState.Error(
                        response.message() ?: "Something went wrong"
                    )
                    Log.d("error", response.toString())
                }
            }
            .onFailure { error ->
                Log.d("error", error.toString())
                _data.value = DataState.Error(
                    error.message ?: "Something went wrong"
                )
            }
    }
}