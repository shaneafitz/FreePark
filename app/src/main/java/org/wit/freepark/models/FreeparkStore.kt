package org.wit.freepark.models

interface  FreeparkStore {
    fun findAll(): List<FreeparkModel>
    fun create(freepark: FreeparkModel)
    fun update(freepark: FreeparkModel)
}