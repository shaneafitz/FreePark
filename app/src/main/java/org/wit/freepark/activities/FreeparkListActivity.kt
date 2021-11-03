package org.wit.freepark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.freepark.R
import org.wit.freepark.adapters.FreeparkAdapter
import org.wit.freepark.adapters.FreeparkListener
import org.wit.freepark.databinding.ActivityFreeparkListBinding
import org.wit.freepark.main.MainApp
import org.wit.freepark.models.FreeparkModel

class FreeparkListActivity : AppCompatActivity(), FreeparkListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityFreeparkListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeparkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadFreeparks()
        registerRefreshCallback()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, FreeParkActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onFreeparkClick(freepark: FreeparkModel) {
        val launcherIntent = Intent(this, FreeParkActivity::class.java)
        launcherIntent.putExtra("freepark_edit", freepark)
        refreshIntentLauncher.launch(launcherIntent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        binding.recyclerView.adapter?.notifyDataSetChanged()
//        super.onActivityResult(requestCode, resultCode, data)
//    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadFreeparks() }
    }

    private fun loadFreeparks() {
        showFreeparks(app.freeparks.findAll())
    }

    fun showFreeparks (freeparks: List<FreeparkModel>) {
        binding.recyclerView.adapter = FreeparkAdapter(freeparks, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}

