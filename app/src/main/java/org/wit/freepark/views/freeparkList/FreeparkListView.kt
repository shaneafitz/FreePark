package org.wit.freepark.views.freeparkList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.freepark.R
import org.wit.freepark.views.freeparkList.FreeparkAdapter
import org.wit.freepark.views.freeparkList.FreeparkListener
import org.wit.freepark.databinding.ActivityFreeparkListBinding
import org.wit.freepark.main.MainApp
import org.wit.freepark.models.FreeparkModel
import timber.log.Timber.i

class FreeparkListView : AppCompatActivity(), FreeparkListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityFreeparkListBinding
    lateinit var presenter: FreeparkListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeparkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = FreeparkListPresenter(this)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadFreeparks()
//        registerRefreshCallback()
//        registerMapCallback()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddFreepark() }
            R.id.item_map -> { presenter.doShowFreeparksMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFreeparkClick(freepark: FreeparkModel) {
        presenter.doEditFreepark(freepark)

    }
    private fun loadFreeparks() {
        binding.recyclerView.adapter = FreeparkAdapter(presenter.getFreeparks(), this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
    override fun onResume() {
        //update the view
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")
        super.onResume()
    }

//    fun showFreeparks (freeparks: List<FreeparkModel>) {
//        binding.recyclerView.adapter = FreeparkAdapter(freeparks, this)
//        binding.recyclerView.adapter?.notifyDataSetChanged()
//    }
}


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        binding.recyclerView.adapter?.notifyDataSetChanged()
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//    private fun registerRefreshCallback() {
//        refreshIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { loadFreeparks() }
//    }
//
//    private fun registerMapCallback() {
//        mapsIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { }
//    }


