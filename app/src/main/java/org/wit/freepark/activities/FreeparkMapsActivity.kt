package org.wit.freepark.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.freepark.R
import org.wit.freepark.databinding.ActivityFreeparkMapsBinding
import org.wit.freepark.databinding.ContentFreeparkMapsBinding
import org.wit.freepark.main.MainApp

class FreeparkMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var contentBinding: ContentFreeparkMapsBinding
    private lateinit var binding: ActivityFreeparkMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp

        binding = ActivityFreeparkMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentFreeparkMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    fun configureMap() {
        map.setOnMarkerClickListener(this)
        map.uiSettings.isZoomControlsEnabled = true
        app.freeparks.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.location).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

//    override fun onMarkerClick(marker: Marker): Boolean {
//        contentBinding.currentLocation.text = marker.title
//
//        return false
//    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val freepark = app.freeparks.findById(tag)
        contentBinding.currentLocation.text = freepark!!.location
        contentBinding.currentDescription.text = freepark!!.description
        //contentBinding.imageView2.setImageBitmap(readImageFromPath(this@FreeparkMapsActivity, freepark.image))
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}



