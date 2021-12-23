package org.wit.freepark.views.freepark

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.freepark.R
import org.wit.freepark.databinding.ActivityFreeparkBinding
import org.wit.freepark.models.FreeparkModel
import timber.log.Timber.i

class FreeparkView : AppCompatActivity() {

    private lateinit var binding: ActivityFreeparkBinding
    lateinit var presenter: FreeparkPresenter
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

        binding.placemarkLocation.setOnClickListener {
            presenter.cacheFreepark(binding.parkingLocation.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
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
                    presenter.doAddOrSave(binding.parkingLocation.text.toString(), binding.description.text.toString())
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showFreepark(freepark: FreeparkModel) {
        binding.parkingLocation.setText(freepark.location)
        binding.description.setText(freepark.description)

        Picasso.get()
            .load(freepark.image)
            .into(binding.freeparkImage)
        if (freepark.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_freepark_image)
        }

    }



    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.freeparkImage)
        binding.chooseImage.setText(R.string.change_freepark_image)
    }

}