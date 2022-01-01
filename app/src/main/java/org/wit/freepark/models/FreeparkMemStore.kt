package org.wit.freepark.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class FreeparkMemStore : FreeparkStore {

    val freeparks = ArrayList<FreeparkModel>()

    override suspend fun findAll(): List<FreeparkModel> {
        return freeparks
    }

    override suspend fun create(freepark: FreeparkModel) {
        freepark.id = getId()
        freeparks.add(freepark)
        logAll()
    }

    override suspend fun update(freepark: FreeparkModel) {
        val foundFreepark: FreeparkModel? = freeparks.find { p -> p.id == freepark.id }
        if (foundFreepark != null) {
            foundFreepark.title = freepark.title
            foundFreepark.description = freepark.description
            foundFreepark.image = freepark.image
            foundFreepark.location = freepark.location
            logAll()
        }
    }

    override suspend fun delete(freepark: FreeparkModel){
        freeparks.remove(freepark)
    }

    private fun logAll() {
        freeparks.forEach { i("${it}") }
    }
    override suspend fun findById(id:Long) : FreeparkModel? {
        val foundFreepark: FreeparkModel? = freeparks.find { it.id == id }
        return foundFreepark
    }
}