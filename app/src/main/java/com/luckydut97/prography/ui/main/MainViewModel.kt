package com.luckydut97.prography.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luckydut97.prography.PrographyApplication
import com.luckydut97.prography.data.api.model.UnsplashPhotoDto
import com.luckydut97.prography.data.repository.PhotoRepository
import com.luckydut97.prography.di.DatabaseModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DatabaseModule.providePhotoRepository(application.applicationContext)

    // 기존 상태들
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _photos = MutableStateFlow<List<UnsplashPhotoDto>>(emptyList())
    val photos: StateFlow<List<UnsplashPhotoDto>> = _photos

    // 페이지네이션을 위한 상태들
    private var currentPage = 1
    private var hasMorePages = true

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    // 북마크 상태
    private val _bookmarks = MutableStateFlow<List<UnsplashPhotoDto>>(emptyList())
    val bookmarks: StateFlow<List<UnsplashPhotoDto>> = _bookmarks

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedPhoto = MutableStateFlow<UnsplashPhotoDto?>(null)
    val selectedPhoto: StateFlow<UnsplashPhotoDto?> = _selectedPhoto

    // 사진 북마크 상태 트래킹
    private val _bookmarkedPhotoIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedPhotoIds: StateFlow<Set<String>> = _bookmarkedPhotoIds

    init {
        loadPhotos()
        loadBookmarks()
    }

    // 북마크 로드
    private fun loadBookmarks() {
        viewModelScope.launch {
            repository.getAllBookmarks().collectLatest { entities ->
                val bookmarkDtos = entities.map { repository.mapEntityToDto(it) }
                _bookmarks.value = bookmarkDtos
                _bookmarkedPhotoIds.value = entities.map { it.id }.toSet()
            }
        }
    }

    // 초기 로딩 함수
    fun loadPhotos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                currentPage = 1
                hasMorePages = true
                val result = repository.getPhotos(page = currentPage, perPage = 10)
                _photos.value = result
                currentPage++
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMorePhotos() {
        if (_isLoadingMore.value || !hasMorePages) {
            return
        }

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                val newPhotos = repository.getPhotos(page = currentPage, perPage = 10)

                if (newPhotos.isEmpty()) {
                    hasMorePages = false
                } else {
                    val existingIds = _photos.value.map { it.id }.toSet()
                    val filteredNewPhotos = newPhotos.filter { it.id !in existingIds }

                    if (filteredNewPhotos.isEmpty()) {
                        hasMorePages = false
                    } else {
                        val updatedList = _photos.value + filteredNewPhotos
                        _photos.value = updatedList
                        currentPage++
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load more photos"
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    // 북마크 토글 함수
    fun toggleBookmark(photo: UnsplashPhotoDto) {
        viewModelScope.launch {
            try {
                val isCurrentlyBookmarked = repository.isBookmarked(photo.id)
                println("*디버깅: toggleBookmark - 현재 상태: $isCurrentlyBookmarked")

                if (isCurrentlyBookmarked) {
                    repository.removeBookmark(photo.id)
                    println("*디버깅: 북마크 제거 시도: ${photo.id}")
                } else {
                    repository.addBookmark(photo)
                    println("*디버깅: 북마크 추가 시도: ${photo.id}")
                }

                // 북마크 상태 즉시 업데이트를 위한 추가 코드
                val updatedBookmarks = repository.getAllBookmarks().first() // Flow에서 첫 번째 값만 가져옴
                val updatedIds = updatedBookmarks.map { it.id }.toSet()
                println("*디버깅: 업데이트된 북마크 IDs: $updatedIds")
                _bookmarkedPhotoIds.value = updatedIds

                // 북마크 목록도 업데이트
                val bookmarkDtos = updatedBookmarks.map { repository.mapEntityToDto(it) }
                _bookmarks.value = bookmarkDtos

            } catch (e: Exception) {
                println("*디버깅: 북마크 처리 중 오류: ${e.message}")
                e.printStackTrace()
                _error.value = "북마크 처리 중 오류가 발생했습니다: ${e.message}"
            }
        }
    }
    // 북마크 상태 확인
    suspend fun isPhotoBookmarked(photoId: String): Boolean {
        return repository.isBookmarked(photoId)
    }

    fun selectPhoto(photo: UnsplashPhotoDto) {
        _selectedPhoto.value = photo
    }

    fun getPhotoById(photoId: String): UnsplashPhotoDto? {
        return photos.value.find { it.id == photoId }
            ?: bookmarks.value.find { it.id == photoId }
    }

    fun retryLoadPhotos() {
        loadPhotos()
    }

    fun clearError() {
        _error.value = null
    }

    // 팩토리 클래스 추가
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}