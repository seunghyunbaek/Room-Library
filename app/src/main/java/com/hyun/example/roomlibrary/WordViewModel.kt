package com.hyun.example.roomlibrary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
ViewModel이란?

    ViewModel의 역할은 UI에 데이터를 제공하는 것이다.

    ViewModel은 리포지토리와 UI간의 커뮤니케이션 센터 역할을 합니다.

    ViewModel을 사용해 프래그먼트간에 데이터를 공유할 수도 있습니다.

 */

class WordViewModel(application:Application) : AndroidViewModel(application) {

    private val repository: WordRepository

    val allWords: LiveData<List<Word>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)

        allWords = repository.allWords
    }

    fun insert(word:Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

}