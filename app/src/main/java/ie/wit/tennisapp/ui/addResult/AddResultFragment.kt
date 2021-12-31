package ie.wit.tennisapp.ui.addResult

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.FragmentAddResultBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.ResultModel
import ie.wit.tennisapp.models.ResultsManager
import ie.wit.tennisapp.ui.results.ResultsViewModel

class AddResultFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentAddResultBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var addResultViewModel: AddResultViewModel

    var result = ResultModel()
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAddResultBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.menu_addResult)

        addResultViewModel = ViewModelProvider(this).get(AddResultViewModel::class.java)
        addResultViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })



        val allMembers = app.members.findAll()
        var memberNames: MutableList<String> = allMembers.map{it.firstName + " " + it.lastName} as MutableList<String>

        memberNames.add(0, getString(R.string.select_a_member))

        val playerOneSpinner = fragBinding.playerOneSpinner
        val playerTwoSpinner = fragBinding.playerTwoSpinner
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, memberNames)
        playerOneSpinner.adapter = adapter
        playerTwoSpinner.adapter = adapter


        if(arguments?.get("result") !== null) {
            val resultId: String = arguments?.get("result") as String
            edit = true
            result = app.results.findById(resultId.toLong())!!
            fragBinding.playerOneSpinner.setSelection(memberNames.indexOf(result.playerOne))
            fragBinding.playerTwoSpinner.setSelection(memberNames.indexOf(result.playerTwo))
            fragBinding.playerOneScore.setText(result.p1Score.toString())
            fragBinding.playerTwoScore.setText(result.p2Score.toString())
            fragBinding.addResultButton.setText(R.string.update_result)
        }

        playerOneSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                result.playerOne = memberNames[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        playerTwoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                result.playerTwo = memberNames[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        setButtonListener(fragBinding)
        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.addResultError),Toast.LENGTH_LONG).show()
        }
    }

    private fun setButtonListener(layout: FragmentAddResultBinding) {
        layout.addResultButton.setOnClickListener() {
            var p1score = layout.playerOneScore.text.toString()
            var p2score = layout.playerTwoScore.text.toString()
            val selectMemberString = activity!!.getString(R.string.select_a_member)
            if (result.playerOne == selectMemberString || result.playerTwo == selectMemberString || p1score.isEmpty() || p2score.isEmpty()) {
                Snackbar
                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                result.p1Score = p1score.toInt()
                result.p2Score = p2score.toInt()
                if (edit) {
//                    app.results.update(result.copy())
                } else {
                    ResultsManager.create(result.copy())
//                    app.results.create(result.copy())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddResultFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        val resultsViewModel = ViewModelProvider(this).get(ResultsViewModel::class.java)
    }
}