package org.wit.freepark.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import org.wit.freepark.databinding.ActivityFreeparkMapsBinding
import org.wit.freepark.databinding.ContentFreeparkMapsBinding
import org.wit.freepark.main.MainApp
import org.wit.freepark.models.FreeparkModel

class FreeparkMapView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var contentBinding: ContentFreeparkMapsBinding
    private lateinit var binding: ActivityFreeparkMapsBinding
//    lateinit var map: GoogleMap
    lateinit var app: MainApp
    lateinit var presenter: FreeparkMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp

        binding = ActivityFreeparkMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = FreeparkMapPresenter(this)

        contentBinding = ContentFreeparkMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            presenter.doPopulateMap(it)
        }
    }
    fun showFreepark(freepark: FreeparkModel) {
        contentBinding.currentLocation.text = freepark.location
        contentBinding.currentDescription.text = freepark.description
        Picasso.get()
            .load(freepark.image)
            .into(contentBinding.imageView2)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
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



