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
        var foundFreepark: FreeparkModel? = freeparks.find { p -> p.id == freepark.id }
        if (foundFreepark != null) {
            foundFreepark.location = freepark.location
            foundFreepark.description = freepark.description
            foundFreepark.image = freepark.image
            logAll()
        }
    }

    fun logAll() {
        freeparks.forEach { i("${it}") }
    }
}