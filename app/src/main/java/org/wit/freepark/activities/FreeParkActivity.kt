package org.wit.freepark.activities

import android.content.Intent
import android.net.Uri
import org.wit.freepark.databinding.ActivityFreeparkBinding
import org.wit.freepark.models.FreeparkModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.freepark.R
import org.wit.freepark.helpers.showImagePicker
import org.wit.freepark.main.MainApp
import org.wit.freepark.models.Location
import timber.log.Timber.i


class FreeParkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeparkBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    var freepark = FreeparkModel()
    lateinit var app: MainApp
    var edit = false
    // var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFreeparkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        registerImagePickerCallback()
        registerMapCallback()

        if (intent.hasExtra("freepark_edit")) {
            edit = true
            freepark = intent.extras?.getParcelable("freepark_edit")!!
            binding.parkingLocation.setText(freepark.location)
            binding.description.setText(freepark.description)
            binding.btnAdd.setText(R.string.save_freepark)
            Picasso.get()
                .load(freepark.image)
                .into(binding.freeparkImage)
            if (freepark.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_freepark_image)
            }
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
            i("add Button Pressed: $freepark")
            setResult(RESULT_OK)
            finish()
        }

        binding.placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (freepark.zoom != 0f) {
                location.lat =  freepark.lat
                location.lng = freepark.lng
                location.zoom = freepark.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
            i("Select image")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_freepark, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.freeparks.delete(freepark)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            freepark.image = result.data!!.data!!
                            Picasso.get()
                                .load(freepark.image)
                                .into(binding.freeparkImage)
                            binding.chooseImage.setText(R.string.change_freepark_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            freepark.lat = location.lat
                            freepark.lng = location.lng
                            freepark.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
