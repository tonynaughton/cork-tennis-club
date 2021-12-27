package ie.wit.tennisapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.tennisapp.R
import ie.wit.tennisapp.activities.AddResultActivity
import ie.wit.tennisapp.activities.RegisterActivity
import ie.wit.tennisapp.adapters.ResultAdapter
import ie.wit.tennisapp.adapters.ResultsListener
import ie.wit.tennisapp.databinding.FragmentResultsBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.ResultModel

class ResultsFragment : Fragment(), ResultsListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentResultsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

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

        fragBinding.btnAdd.setOnClickListener() {
            val launcherIntent = Intent(context, AddResultActivity::class.java)
            startActivityForResult(launcherIntent, 0)
        }

        loadResults()
        registerRefreshCallback()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResultsFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onEditResultClick(result: ResultModel) {
        val launcherIntent = Intent(context, AddResultActivity::class.java)
        launcherIntent.putExtra("result_edit", result)
        refreshIntentLauncher.launch(launcherIntent)
    }

    override fun onDeleteResultClick(result: ResultModel) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setMessage("Are you sure you want to delete this result?")?.setCancelable(false)
            ?.setPositiveButton("Yes") { _, _ ->
                app.results.delete(result)
                fragBinding.recyclerView.adapter?.notifyDataSetChanged()
            }?.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder?.create()?.show()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadResults() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadResults() {
        showResults(app.results.findAll())
    }

    private fun showResults (results: List<ResultModel>) {
        fragBinding.recyclerView.adapter = ResultAdapter(results, this)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }
}