package com.example.olimpitmo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olimpitmo.data.MatchesRepository
import com.example.olimpitmo.data.MatchesRepositoryImpl
import com.example.olimpitmo.data.Result
import com.example.olimpitmo.data.remote.DatesMatch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MatchesViewModel (
    private val repository: MatchesRepository
) : ViewModel() {

    private val _matchesList = MutableStateFlow<List<DatesMatch>>(emptyList())
    val matchesList = _matchesList.asStateFlow()

    private val _matchesListByDate = MutableStateFlow<List<DatesMatch>>(emptyList())
    val matchesListByDate = _matchesListByDate.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError= _showError.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()
    private val _completedMatches = MutableStateFlow<List<DatesMatch>>(emptyList())
    val completedMatches= _completedMatches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadInitialMatches()
    }

    fun loadInitialMatches() {
        viewModelScope.launch {
            repository.getListMatches().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _matchesList.value = result.data!!
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.message
                        _showError.value = true
                    }
                }
            }
        }
    }

    fun getMatchesByDate(date: String) {
        viewModelScope.launch {
            repository.getListMatchesByLeagueDate(date).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _matchesListByDate.value = result.data!!
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.message
                        _showError.value = true
                    }
                }
            }
        }
    }
    fun getCompletedMatches(from: String, to: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getCompletedMatches(from, to).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _completedMatches.value = result.data!!
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.message
                        _showError.value = true
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun clearError() {
        _showError.value = false
        _errorMessage.value = null
    }
    fun clearCompletedMatches() {
        _completedMatches.value = emptyList()
    }
}