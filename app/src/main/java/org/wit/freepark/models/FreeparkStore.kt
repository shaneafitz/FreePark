package org.wit.freepark.models

interface  FreeparkStore {
   suspend fun findAll(): List<FreeparkModel>
   suspend fun create(freepark: FreeparkModel)
   suspend fun update(freepark: FreeparkModel)
   suspend fun delete(freepark: FreeparkModel)
   suspend fun findById(id:Long) : FreeparkModel?
   suspend fun clear()
}