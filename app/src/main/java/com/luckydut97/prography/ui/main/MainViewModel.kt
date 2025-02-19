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

    // 기존 상태들
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _photos = MutableStateFlow<List<UnsplashPhotoDto>>(emptyList())
    val photos: StateFlow<List<UnsplashPhotoDto>> = _photos

    // 페이지네이션을 위한 새로운 상태들
    private var currentPage = 1
    private var hasMorePages = true

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    // 나머지 기존 상태들
    private val _bookmarks = MutableStateFlow<List<UnsplashPhotoDto>>(emptyList())
    val bookmarks: StateFlow<List<UnsplashPhotoDto>> = _bookmarks

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedPhoto = MutableStateFlow<UnsplashPhotoDto?>(null)
    val selectedPhoto: StateFlow<UnsplashPhotoDto?> = _selectedPhoto

    init {
        loadPhotos()
    }

    // 초기 로딩 함수 수정
    fun loadPhotos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                currentPage = 1  // 페이지 초기화
                hasMorePages = true  // 더 있음 표시 초기화
                val result = repository.getPhotos(page = currentPage, perPage = 10)
                _photos.value = result
                currentPage++  // 다음 페이지 준비
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMorePhotos() {
        if (_isLoadingMore.value || !hasMorePages) {
            println("*로그: 무한 스크롤: 이미 로딩 중이거나 더 이상 페이지 없음 (로딩 중: ${_isLoadingMore.value}, 더 있음: $hasMorePages)")
            return
        }

        viewModelScope.launch {
            try {
                println("*로그: 무한 스크롤: 페이지 $currentPage 로딩 시작")
                _isLoadingMore.value = true

                val newPhotos = repository.getPhotos(page = currentPage, perPage = 10)
                println("*로그: 무한 스크롤: 페이지 ${currentPage}에서 ${newPhotos.size}개 로드됨")

                if (newPhotos.isEmpty()) {
                    hasMorePages = false
                    println("*로그: 무한 스크롤: 더 이상 데이터 없음")
                } else {
                    val existingIds = _photos.value.map { it.id }.toSet()
                    val filteredNewPhotos = newPhotos.filter { it.id !in existingIds }
                    println("*로그: 무한 스크롤: 중복 제외 후 ${filteredNewPhotos.size}개")

                    if (filteredNewPhotos.isEmpty()) {
                        hasMorePages = false
                        println("*로그: 무한 스크롤: 새 항목 없음 (모두 중복)")
                    } else {
                        val updatedList = _photos.value + filteredNewPhotos
                        println("*로그: 무한 스크롤: 업데이트 ${_photos.value.size} → ${updatedList.size}")
                        _photos.value = updatedList
                        currentPage++
                        println("*로그: 무한 스크롤: 다음 페이지는 $currentPage")
                    }
                }
            } catch (e: Exception) {
                println("*로그: 무한 스크롤 오류: ${e.message}")
                e.printStackTrace()
                _error.value = e.message ?: "Failed to load more photos"
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    // 기존 함수들
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