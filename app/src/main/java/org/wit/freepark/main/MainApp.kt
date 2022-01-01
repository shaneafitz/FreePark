package org.wit.freepark.main

import android.app.Application
import org.wit.freepark.models.FreeparkJSONStore
import org.wit.freepark.models.FreeparkMemStore
import org.wit.freepark.models.FreeparkStore
import org.wit.freepark.room.FreeparkStoreRoom
//import org.wit.freepark.models.FreeparkModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {


    lateinit var freeparks: FreeparkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        freeparks = FreeparkStoreRoom(applicationContext)
        i("FreePark started")
//        freeparks.add(FreeparkModel("One", "About one..."))
//        freeparks.add(FreeparkModel("Two", "About two..."))
//        freeparks.add(FreeparkModel("Three", "About three..."))
    }
}