package ie.wit.tennisapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.tennisapp.R
import ie.wit.tennisapp.activities.AddResultActivity
import ie.wit.tennisapp.adapters.ResultAdapter
import ie.wit.tennisapp.adapters.ResultsListener
import ie.wit.tennisapp.databinding.FragmentResultsBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.ResultModel

class ResultsFragment : Fragment(), ResultsListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentResultsBinding? = null
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

        _fragBinding = FragmentResultsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.menu_results)

        val layoutManager = LinearLayoutManager(context)
        fragBinding.recyclerView.layoutManager = layoutManager
        fragBinding.recyclerView.adapter = ResultAdapter(app.results.findAll(), this)

//        fragBinding.btnAdd.setOnClickListener() {
//            val launcherIntent = Intent(context, AddResultActivity::class.java)
//            startActivityForResult(launcherIntent, 0)
//        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResultsFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onEditResultClick(result: ResultModel) {
        TODO("Not yet implemented")
    }

    override fun onDeleteResultClick(result: ResultModel) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }
}