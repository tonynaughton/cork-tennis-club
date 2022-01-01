package ie.wit.tennisapp.ui.contact

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.FragmentContactBinding
import ie.wit.tennisapp.main.MainApp

class ContactFragment : Fragment() {

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

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            val location = LatLng(51.87924593836583, -8.535871569744797)
            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Cork Tennis Club")
            )

            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}