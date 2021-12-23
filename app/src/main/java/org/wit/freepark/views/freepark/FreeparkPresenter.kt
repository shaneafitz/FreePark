package org.wit.freepark.views.freepark
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.wit.freepark.databinding.ActivityFreeparkBinding
import org.wit.freepark.helpers.showImagePicker
import org.wit.freepark.main.MainApp
import org.wit.freepark.models.Location
import org.wit.freepark.models.FreeparkModel
import org.wit.freepark.views.editLocation.EditLocationView
//import org.wit.freepark.showImagePicker
//import org.wit.freepark.EditLocationView
import timber.log.Timber

class FreeparkPresenter(private val view: FreeparkView) {

    var freepark = FreeparkModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityFreeparkBinding = ActivityFreeparkBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("freepark_edit")) {
            edit = true
            freepark = view.intent.extras?.getParcelable("freepark_edit")!!
            view.showFreepark(freepark)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSave(location: String, description: String) {
        freepark.location = location
        freepark.description = description
        if (edit) {
            app.freeparks.update(freepark)
        } else {
            app.freeparks.create(freepark)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.freeparks.delete(freepark)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }
        fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (freepark.zoom != 0f) {
            location.lat =  freepark.lat
            location.lng = freepark.lng
            location.zoom = freepark.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }
    fun cacheFreepark (location: String, description: String) {
        freepark.location = location;
        freepark.description = description
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            freepark.image = result.data!!.data!!
                            view.updateImage(freepark.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            freepark.lat = location.lat
                            freepark.lng = location.lng
                            freepark.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }
}