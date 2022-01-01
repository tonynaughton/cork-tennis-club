package ie.wit.tennisapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.wit.tennisapp.databinding.ActivityWelcomeBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.ui.home.Home

class WelcomeActivity: AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var authenticationViewModel : AuthenticationViewModel

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

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        authenticationViewModel.liveFirebaseUser.observe(this, Observer {
                firebaseUser -> if (firebaseUser != null)
            startActivity(Intent(this, Home::class.java))
        })
    }
}