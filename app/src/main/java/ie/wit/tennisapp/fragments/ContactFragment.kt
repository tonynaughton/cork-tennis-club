package ie.wit.tennisapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.FragmentContactBinding
import ie.wit.tennisapp.databinding.FragmentMembersBinding
import ie.wit.tennisapp.main.MainApp

class ContactFragment : Fragment(), OnMapReadyCallback {

    lateinit var app: MainApp
    private var _fragBinding: FragmentContactBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentContactBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.menu_contact)

        val mapFragment = parentFragmentManager
            .findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return root
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

    companion object {
        @JvmStatic
        fun newInstance() =
            MembersFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}