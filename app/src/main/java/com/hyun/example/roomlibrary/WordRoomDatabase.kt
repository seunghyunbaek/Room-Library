package com.hyun.example.roomlibrary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
    Room Database

    Room 은 SQLite 데이터베이스 위에 있는 데이터베이스 계층입니다.

    Room은 SQLiteOpenHelper로 처리하는 일상적인 작업을 처리합니다.

    Room은 DAO를 사용하여 데이터베이스에 쿼리를 실행합니다.

    기본적으로 UI 성능 저하를 피하기 위해 Room에서는 기본 스레드에서 쿼리를 실행할 수 없습니다.
    룸 쿼리가 LiveData를 반환하면 쿼리가 백그라운드 스레드에서 자동으로 비동기적으로 실행됩니다.

    Room은 SQLite 문의 컴파일 타임 검사를 제공합니다.
 */
@Database(entities = [Word::class], version = 1)
public abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    // 동시간대에 여러 인스턴스를 방지하기. Singleton
    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "Word_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
//                return instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }


        suspend fun populateDatabase(wordDao: WordDao) {
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World1")
            wordDao.insert(word)
        }
    }

}