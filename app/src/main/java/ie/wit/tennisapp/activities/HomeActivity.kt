package ie.wit.tennisapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityHomeBinding
import ie.wit.tennisapp.main.MainApp

class HomeActivity: AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            R.id.item_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}