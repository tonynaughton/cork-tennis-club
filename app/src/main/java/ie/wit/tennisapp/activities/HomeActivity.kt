package ie.wit.tennisapp.activities

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
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

        app = application as MainApp
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_view_results -> {
                startActivity(Intent(this, ListResultsActivity::class.java))
                true
            }
            R.id.item_view_members -> {
                startActivity(Intent(this, ListMembersActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}