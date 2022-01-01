package org.wit.freepark.room

import androidx.room.*
import org.wit.freepark.models.FreeparkModel

@Dao
interface FreeparkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(freepark: FreeparkModel)

    @Query("SELECT * FROM FreeparkModel")
    suspend fun findAll(): List<FreeparkModel>

    @Query("select * from FreeparkModel where id = :id")
    suspend fun findById(id: Long): FreeparkModel

    @Update
    suspend fun update(freepark: FreeparkModel)

    @Delete
    suspend fun deleteFreepark(freepark: FreeparkModel)
}