package ie.wit.tennisapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import ie.wit.tennisapp.databinding.ActivityContactBinding
import ie.wit.tennisapp.main.MainApp
import com.google.android.gms.maps.SupportMapFragment
import ie.wit.tennisapp.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class ContactActivity: AppCompatActivity(), OnMapReadyCallback {

    lateinit var app: MainApp
    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        app = application as MainApp
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(51.87924593836583, -8.535871569744797)
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Marker")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
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
            R.id.item_contact -> {
                startActivity(Intent(this, ContactActivity::class.java))
                true
            }
            R.id.item_logout -> {
                startActivity(Intent(this, HomeActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}