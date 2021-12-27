package ie.wit.tennisapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.wit.tennisapp.R
import ie.wit.tennisapp.auth.LoginRegisterViewModel
import ie.wit.tennisapp.databinding.ActivityWelcomeBinding
import ie.wit.tennisapp.main.MainApp

class WelcomeActivity: AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var loginRegisterViewModel : LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        app = application as MainApp
    }

    override fun onStart() {
        super.onStart()

        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loginRegisterViewModel.liveFirebaseUser.observe(this, Observer {
                firebaseUser -> if (firebaseUser != null)
            startActivity(Intent(this, Home::class.java))
        })
    }
}