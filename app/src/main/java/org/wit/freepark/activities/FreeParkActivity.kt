package org.wit.freepark.activities

import org.wit.freepark.databinding.ActivityFreeparkBinding
import org.wit.freepark.models.FreeparkModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import org.wit.freepark.R
import org.wit.freepark.main.MainApp
import timber.log.Timber.i


class FreeParkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeparkBinding
    var freepark = FreeparkModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityFreeparkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        if (intent.hasExtra("freepark_edit")) {
            edit = true
            freepark = intent.extras?.getParcelable("freepark_edit")!!
            binding.parkingLocation.setText(freepark.location)
            binding.description.setText(freepark.description)
            binding.btnAdd.setText(R.string.save_freepark)
        }
        binding.btnAdd.setOnClickListener() {
            freepark.location = binding.parkingLocation.text.toString()
            freepark.description = binding.description.text.toString()
            if (freepark.location.isEmpty()) {
                Snackbar.make(it, R.string.enter_freepark_location, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.freeparks.update(freepark.copy())
                } else {
                    app.freeparks.create(freepark.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_freepark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
