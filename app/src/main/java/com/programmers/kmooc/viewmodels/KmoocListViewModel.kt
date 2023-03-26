package com.programmers.kmooc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.programmers.kmooc.models.LectureList
import com.programmers.kmooc.models.LectureSimple
import com.programmers.kmooc.repositories.KmoocRepository

class KmoocListViewModel(private val repository: KmoocRepository) : ViewModel() {

    private var lectureResponse: LectureList? = null

    private val _lectureList: MutableLiveData<List<LectureSimple>> = MutableLiveData<List<LectureSimple>>()
    val lectureList: LiveData<List<LectureSimple>> get() = _lectureList

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun list() {
        _isLoading.postValue(true)

        repository.list { lecture ->
            lecture?.let {
                lectureResponse = lecture

                _lectureList.postValue(lecture.lectures)
            }
            _isLoading.postValue(false)
        }
    }

    fun next() {
        _isLoading.postValue(true)

        if (lectureResponse != null && lectureResponse?.next?.isEmpty() == false) {
            repository.next(lectureResponse!!.next) { lecture ->
                lecture?.let {
                    lectureResponse = lecture

                    _lectureList.postValue(_lectureList.value?.plus(lecture.lectures))
                }
                _isLoading.postValue(false)
            }
        }
    }
}

class KmoocListViewModelFactory(private val repository: KmoocRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KmoocListViewModel::class.java)) {
            return KmoocListViewModel(repository) as T
        }
        throw IllegalAccessException("Unkown Viewmodel Class")
    }
}