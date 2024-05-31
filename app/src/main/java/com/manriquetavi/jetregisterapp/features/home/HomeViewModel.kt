package com.manriquetavi.jetregisterapp.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manriquetavi.jetregisterapp.domain.model.Form
import com.manriquetavi.jetregisterapp.domain.model.Response
import com.manriquetavi.jetregisterapp.domain.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FirestoreRepository
): ViewModel() {
    private val _homeUiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Success)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    fun saveInformation(
        form: Form
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            _homeUiState.value = HomeUiState.Loading
            repository.addInformation(form).collect { response ->
                _homeUiState.value = when (response) {
                    is Response.Success -> HomeUiState.Success
                    is Response.Loading -> HomeUiState.Loading
                    is Response.Error -> HomeUiState.Error
                }
            }

        }
    }
}