package org.wit.freepark.room

import android.content.Context
import androidx.room.Room
import org.wit.freepark.models.FreeparkModel
import org.wit.freepark.models.FreeparkStore

class FreeparkStoreRoom(val context: Context) : FreeparkStore {

    var dao: FreeparkDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.freeparkDao()
    }

    override suspend fun findAll(): List<FreeparkModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): FreeparkModel? {
        return dao.findById(id)
    }

    override suspend fun create(freepark: FreeparkModel) {
        dao.create(freepark)
    }

    override suspend fun  update(freepark: FreeparkModel) {
        dao.update(freepark)
    }

    override suspend fun delete(freepark: FreeparkModel) {
        dao.deleteFreepark(freepark)
    }
    override suspend fun clear(){

    }

}