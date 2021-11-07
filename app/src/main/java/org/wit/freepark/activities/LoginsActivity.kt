package org.wit.freepark.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.freepark.R
import org.wit.freepark.databinding.ActivityLoginsBinding
import org.wit.freepark.main.MainApp

class LoginsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginsBinding
    lateinit var app: MainApp
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginsBinding.inflate(layoutInflater)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        app = application as MainApp

        mAuth = Firebase.auth
        setUpToolbar()

        binding.txtRegister.setOnClickListener {
            val intent = Intent(this@LoginsActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

//        val email = etEmail.text.toString()
//        val password = etPassword.text.toString()

        binding.btnLogin.setOnClickListener {
            mAuth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@LoginsActivity, FreeparkListActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@LoginsActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        setUpToolbar()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    // function so nothing happens when backpressed after login
    override fun onBackPressed() {
// empty so nothing happens
    }

    private fun logout() {
        mAuth.signOut()
    }
}