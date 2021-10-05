package org.wit.freepark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber
import timber.log.Timber.i

class FreeParkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freepark)

        Timber.plant(Timber.DebugTree())

        i("FreePark Activity started..")
    }
}