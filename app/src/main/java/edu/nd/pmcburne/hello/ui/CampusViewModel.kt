package edu.nd.pmcburne.hello.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.CampusRepository
import edu.nd.pmcburne.hello.LocationWithTag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CampusViewModel(
    private val repository: CampusRepository
) : ViewModel() {

    private val _selectedTag = MutableStateFlow("core")
    val selectedTag: StateFlow<String> = _selectedTag

    val tags = repository.getAllTags()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val locations = _selectedTag
        .flatMapLatest { tag ->
            repository.getLocationsForTag(tag)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList<LocationWithTag>()
        )

    init {
        viewModelScope.launch {
            repository.syncFromApi()
        }
    }

    fun updateSelectedTag(tag: String) {
        _selectedTag.value = tag
    }
}

class CampusViewModelFactory(
    private val repository: CampusRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CampusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CampusViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}