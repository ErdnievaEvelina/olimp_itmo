package com.example.olimpitmo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class MatchesViewModel(
    private val matchesRepository: MatchesRepositoryImpl
): ViewModel() {
    private val _matchesList = MutableStateFlow<List<DatesMatch>>(emptyList())
    val matchesList = _matchesList.asStateFlow()
    private val _matchesListByLegue = MutableStateFlow<List<DatesMatch>>(emptyList())
    val matchesListByLegue = _matchesListByLegue.asStateFlow()

    private val _showError = Channel<Boolean>()
    val showError = _showError.receiveAsFlow()

    init {
        viewModelScope.launch {
            matchesRepository.getListMatches().collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _showError.send(true)
                    }

                    is Result.Success -> {
                        result.data?.let { m ->
                            _matchesList.update { m }
                        }
                    }
                }
            }
        }
        getMatchesByLeagueDate()
    }

    fun getMatchesByLeagueDate(from: String="2026-02-05"){
        viewModelScope.launch {
            matchesRepository.getListMatchesByLeagueDate(from).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _showError.send(true)
                    }

                    is Result.Success -> {
                        result.data?.let { m ->
                            _matchesListByLegue.update { m }
                        }
                    }
                }
            }
        }

    }
}