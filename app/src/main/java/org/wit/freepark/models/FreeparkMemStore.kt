package org.wit.freepark.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class FreeparkMemStore : FreeparkStore {

    val freeparks = ArrayList<FreeparkModel>()

    override fun findAll(): List<FreeparkModel> {
        return freeparks
    }

    override fun create(freepark: FreeparkModel) {
        freepark.id = getId()
        freeparks.add(freepark)
        logAll()
    }

    override fun update(freepark: FreeparkModel) {
        val foundFreepark: FreeparkModel? = freeparks.find { p -> p.id == freepark.id }
        if (foundFreepark != null) {
            foundFreepark.location = freepark.location
            foundFreepark.description = freepark.description
            foundFreepark.image = freepark.image
            foundFreepark.lat = freepark.lat
            foundFreepark.lng = freepark.lng
            foundFreepark.zoom = freepark.zoom
            logAll()
        }
    }

    override fun delete(freepark: FreeparkModel){
        freeparks.remove(freepark)
    }

    fun logAll() {
        freeparks.forEach { i("${it}") }
    }
    override fun findById(id:Long) : FreeparkModel? {
        val foundFreepark: FreeparkModel? = freeparks.find { it.id == id }
        return foundFreepark
    }
}