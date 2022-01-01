package org.wit.freepark.views.freeparkList

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.freepark.views.map.FreeparkMapView
import org.wit.freepark.views.freepark.FreeparkView
import org.wit.freepark.main.MainApp
import org.wit.freepark.models.FreeparkModel
import org.wit.freepark.views.login.LoginView

class FreeparkListPresenter(private val view: FreeparkListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
//    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    suspend fun getFreeparks() = app.freeparks.findAll()

    fun doAddFreepark() {
        val launcherIntent = Intent(view, FreeparkView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditFreepark(freepark: FreeparkModel) {
        val launcherIntent = Intent(view, FreeparkView::class.java)
        launcherIntent.putExtra("freepark_edit", freepark)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowFreeparksMap() {
        val launcherIntent = Intent(view, FreeparkMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doLogout(){
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getFreeparks()
                }
            }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}