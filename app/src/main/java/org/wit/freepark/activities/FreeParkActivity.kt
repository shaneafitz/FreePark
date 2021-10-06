package org.wit.freepark.activities

import org.wit.freepark.databinding.ActivityFreeparkBinding
import org.wit.freepark.models.FreeparkModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import timber.log.Timber.i


class FreeParkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeparkBinding
    var freepark = FreeparkModel()
    val freeparks = ArrayList<FreeparkModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFreeparkBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_freepark)

        Timber.plant(Timber.DebugTree())

        i("FreePark Activity started..")

        binding.btnAdd.setOnClickListener() {
            freepark.location = binding.parkingLocation.text.toString()
            freepark.description = binding.description.text.toString()
            if (freepark.location.isNotEmpty()) {
                freeparks.add(freepark.copy())
                i("add Button Pressed: ${freepark}")
                for (i in freeparks.indices)
                { i("Freepark[$i]:${this.freeparks[i]}") }
            }
            else {
                Snackbar
                    .make(it,"Please enter a location", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
