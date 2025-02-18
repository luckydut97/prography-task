package com.luckydut97.prography.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.prography.data.api.model.UnsplashPhotoDto
import com.luckydut97.prography.data.repository.PhotoRepository
import com.luckydut97.prography.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = PhotoRepository(NetworkModule.unsplashApi)

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _photos = MutableStateFlow<List<UnsplashPhotoDto>>(emptyList())
    val photos: StateFlow<List<UnsplashPhotoDto>> = _photos

    private val _bookmarks = MutableStateFlow<List<UnsplashPhotoDto>>(emptyList())
    val bookmarks: StateFlow<List<UnsplashPhotoDto>> = _bookmarks

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedPhoto = MutableStateFlow<UnsplashPhotoDto?>(null)
    val selectedPhoto: StateFlow<UnsplashPhotoDto?> = _selectedPhoto

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val result = repository.getPhotos()
                _photos.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleBookmark(photo: UnsplashPhotoDto) {
        viewModelScope.launch {
            val currentBookmarks = _bookmarks.value.toMutableList()
            if (currentBookmarks.any { it.id == photo.id }) {
                currentBookmarks.removeAll { it.id == photo.id }
            } else {
                currentBookmarks.add(photo)
            }
            _bookmarks.value = currentBookmarks
        }
    }

    fun selectPhoto(photo: UnsplashPhotoDto) {
        _selectedPhoto.value = photo
    }

    fun getPhotoById(photoId: String): UnsplashPhotoDto? {
        return photos.value.find { it.id == photoId }
    }

    fun retryLoadPhotos() {
        loadPhotos()
    }

    fun clearError() {
        _error.value = null
    }
}