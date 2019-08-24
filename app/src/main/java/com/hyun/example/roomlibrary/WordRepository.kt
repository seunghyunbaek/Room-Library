package com.hyun.example.roomlibrary

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

/*
Repository

리포지토리 클래스는 여러 데이터 소스에 접근할수 있습니다.
리포지토리는 아키텍처 구성요소 라이브러리의 일부가 아니지만 코드 분리 및 아키텍처에 대한 권장 모범 사례입니다.
Repository 크래스는 응용프로그램의 나머지 부분에 대한 데이터 액세스를 위한 깨끗한 API를 제공합니다.

왜 사용할까?
리포지토리는 쿼리를 관리하고 여러 백엔드를 사용할 수 있도록 합니다.
가장 일반적인 예에서 리포지토리는 네트워크에서 데이터를 가져오거나 로컬 데이터베이스에 캐시된 결과를 사용할지 여부를 결정하기위한 논리를 구현합니다.
 */


// Room은 모든 쿼리를 별도의 스레드에서 실행합니다.
// LiveData는 데이터가 면경되면 관찰자에게 알립니다.
class WordRepository (private val wordDao:WordDao) {
    // LiveData는 데이터가 면경되면 관찰자에게 알립니다.
    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    /*
        UI가 아닌 스레드에서 이것을 호출해야 합니다.
        그렇지 않으면 앱이 중단됩니다.
        Room은 메인 스레드에서 장기 실행 작업을 수행하지 않도록하여 UI를 차단합니다.
        @WorkerThread는 비UI 스레드에서 호출해야함을 표시합니다.
        suspend를 써서 컴파일러나 코루틴에게 비동기 실행을 위한 중단지점임을 알려줍니다.
     */
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }


}