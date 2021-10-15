package org.wit.freepark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeparkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = FreeparkAdapter(app.freeparks.findAll(), this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, FreeParkActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onFreeparkClick(freepark: FreeparkModel) {
        val launcherIntent = Intent(this, FreeParkActivity::class.java)
        launcherIntent.putExtra("freepark_edit", freepark)
        startActivityForResult(launcherIntent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}

