package org.wit.freepark.views.freepark

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.freepark.databinding.ActivityFreeparkBinding
import org.wit.freepark.helpers.checkLocationPermissions
import org.wit.freepark.helpers.createDefaultLocationRequest
import org.wit.freepark.helpers.showImagePicker
import org.wit.freepark.main.MainApp
import org.wit.freepark.models.Location
import org.wit.freepark.models.FreeparkModel
import org.wit.freepark.views.editLocation.EditLocationView
//import org.wit.freepark.showImagePicker
//import org.wit.freepark.EditLocationView
import timber.log.Timber
import timber.log.Timber.i

class FreeparkPresenter(private val view: FreeparkView) {

    var freepark = FreeparkModel()
    var map: GoogleMap? = null
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var app: MainApp = view.application as MainApp
    var binding: ActivityFreeparkBinding = ActivityFreeparkBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {
        if (view.intent.hasExtra("freepark_edit")) {
            edit = true
            freepark = view.intent.extras?.getParcelable("freepark_edit")!!
            view.showFreepark(freepark)
        }else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            freepark.lat = location.lat
            freepark.lng = location.lng
        }
        if (checkLocationPermissions(view)) {
            doSetCurrentLocation()
        }
        registerImagePickerCallback()
        registerMapCallback()
        doPermissionLauncher()
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(freepark.lat, freepark.lng)
    }
    fun locationUpdate(lat: Double, lng: Double) {
        freepark.lat = lat
        freepark.lng = lng
        freepark.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(freepark.location).position(LatLng(freepark.lat, freepark.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(freepark.lat, freepark.lng), freepark.zoom))
        view?.showFreepark(freepark)
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
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
    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        i("setting location from doSetLocation")
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
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
        if (freepark.zoom != 0f) {
            location.lat =  freepark.lat
            location.lng = freepark.lng
            location.zoom = freepark.zoom
            locationUpdate(freepark.lat, freepark.lng)
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
    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}