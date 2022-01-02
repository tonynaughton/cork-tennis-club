package ie.wit.tennisapp.ui.addResult

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.FragmentAddResultBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.ResultModel
import java.text.SimpleDateFormat
import java.util.*

class AddResultFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentAddResultBinding? = null
    private val fragBinding get() = _fragBinding!!

    var result = ResultModel()
    var edit = false
    var cal: Calendar = Calendar.getInstance()
    private var resultDate: TextView? = null
    private var buttonAddDate: Button? = null

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
        if(arguments?.get("result") !== null) {
            edit = true
        }
        if (edit) {
            activity?.title = getString(R.string.menu_editResult)
        } else {
            activity?.title = getString(R.string.add_result)
        }

        val allMembers = app.members.findAll()
        var memberNames: MutableList<String> = allMembers.map{it.firstName + " " + it.lastName} as MutableList<String>

        memberNames.add(0, getString(R.string.select_a_member))

        val playerOneSpinner = fragBinding.playerOneSpinner
        val playerTwoSpinner = fragBinding.playerTwoSpinner
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, memberNames)
        playerOneSpinner.adapter = adapter
        playerTwoSpinner.adapter = adapter

        resultDate = fragBinding.resultDate
        buttonAddDate = fragBinding.addDateButton

        resultDate!!.text = "--/--/----"

        // Date picker implemented with reference to https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        buttonAddDate!!.setOnClickListener {
            DatePickerDialog(
                context!!,
                R.style.datePickerStyle,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        if(edit) {
            val resultId: String = arguments?.get("result") as String
            result = app.results.findById(resultId.toLong())!!
            fragBinding.playerOneSpinner.setSelection(memberNames.indexOf(result.playerOne))
            fragBinding.playerTwoSpinner.setSelection(memberNames.indexOf(result.playerTwo))
            fragBinding.playerOneScore.setText(result.p1Score.toString())
            fragBinding.playerTwoScore.setText(result.p2Score.toString())
            resultDate!!.text = result.date
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

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        resultDate!!.text = sdf.format(cal.time)
    }

    private fun setButtonListener(layout: FragmentAddResultBinding) {
        layout.addResultButton.setOnClickListener() {
            val p1score = layout.playerOneScore.text.toString()
            val p2score = layout.playerTwoScore.text.toString()
            val resultDate = layout.resultDate.text
            val selectMemberString = activity!!.getString(R.string.select_a_member)
            if (result.playerOne == selectMemberString || result.playerTwo == selectMemberString || p1score.isEmpty() || p2score.isEmpty() || resultDate.isEmpty()) {
                Snackbar
                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                result.p1Score = p1score.toInt()
                result.p2Score = p2score.toInt()
                result.date = resultDate.toString()
                if (edit) {
                    app.results.update(result.copy())
                } else {
                    app.results.create(result.copy())
                }
                findNavController().popBackStack()
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
}