package com.example.dictionaryapp.core.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dictionaryapp.feature.dictionary.data.local.WordInfoDao
import com.example.dictionaryapp.feature.dictionary.data.local.entity.WordInfoEntity

@Database(
    entities = [WordInfoEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract val wordInfoDao: WordInfoDao
}