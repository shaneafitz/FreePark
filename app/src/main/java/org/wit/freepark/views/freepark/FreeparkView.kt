package org.wit.freepark.views.freepark

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.freepark.R
import org.wit.freepark.databinding.ActivityFreeparkBinding
import org.wit.freepark.models.FreeparkModel
import timber.log.Timber.i

class FreeparkView : AppCompatActivity() {

    private lateinit var binding: ActivityFreeparkBinding
    lateinit var presenter: FreeparkPresenter
    lateinit var map: GoogleMap
    var freepark = FreeparkModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFreeparkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = FreeparkPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheFreepark(binding.parkingLocation.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.mapView2.setOnClickListener {
            presenter.cacheFreepark(binding.parkingLocation.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }
        binding.mapView2.onCreate(savedInstanceState);
        binding.mapView2.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_freepark, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.parkingLocation.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_freepark_location, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doAddOrSave(
                            binding.parkingLocation.text.toString(),
                            binding.description.text.toString()
                        )
                    }
                }
            }
            R.id.item_delete -> {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doDelete()
                }
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showFreepark(freepark: FreeparkModel) {
        if (binding.parkingLocation.text.isEmpty()) binding.parkingLocation.setText(freepark.title)
        if (binding.description.text.isEmpty()) binding.description.setText(freepark.description)

        Picasso.get()
            .load(freepark.image)
            .into(binding.freeparkImage)
        if (freepark.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_freepark_image)
        }
        binding.lat.setText("%.6f".format(freepark.location.lat))
        binding.lng.setText("%.6f".format(freepark.location.lng))

    }



    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.freeparkImage)
        binding.chooseImage.setText(R.string.change_freepark_image)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }

}