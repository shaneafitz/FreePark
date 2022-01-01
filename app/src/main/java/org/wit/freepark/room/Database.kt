package org.wit.freepark.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.wit.freepark.helpers.Converters
import org.wit.freepark.models.FreeparkModel

@Database(entities = arrayOf(FreeparkModel::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun freeparkDao(): FreeparkDao
}