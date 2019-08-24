package com.hyun.example.roomlibrary

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/*
DAO(Data Access Object)

DAO는 인터페이스 또는 추상 클래스 여야합니다.

기본적으로 모든 쿼리는 별도의 스레드에서 실행해야합니다.

Room은 DAO를 사용하여 코드에 대한 깨끗한 API를 만듭니다.
 */

/*
LiveData 클래스

데이터가 변경되면 일반적으로 업데이트 된 데이터를 UI에 표시하는 등의 작업을 수행하려고 합니다.
즉, 데이터가 변경될 때 반응 할 수 있도록 데이터를 관찰해야 합니다.

 */
@Dao
interface WordDao {

    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Insert
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    fun deleteAll()
}