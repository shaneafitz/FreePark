package org.wit.freepark.views.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.freepark.databinding.ActivityLoginsBinding

class LoginView : AppCompatActivity(){
    lateinit var presenter: LoginPresenter
    private lateinit var binding: ActivityLoginsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        presenter = LoginPresenter( this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        binding.signUp.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email == "" || password == "") {
                showSnackBar("please provide email and password")
            }
            else {
                presenter.doSignUp(email,password)
            }
        }

        binding.logIn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email == "" || password == "") {
                showSnackBar("please provide email and password")
            }
            else {
                presenter.doLogin(email,password)
            }
        }
    }
    fun showSnackBar(message: CharSequence){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .show()
    }
     fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

     fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }
}