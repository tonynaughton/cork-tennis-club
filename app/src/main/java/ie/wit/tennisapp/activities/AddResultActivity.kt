package ie.wit.tennisapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityAddResultBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.ResultModel

class AddResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddResultBinding
    var result = ResultModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        val allMembers = app.members.findAll()
        var memberNames: MutableList<String> = mutableListOf()
        memberNames = allMembers.map{it.firstName + " " + it.lastName} as MutableList<String>
        val playerOneSpinner = findViewById<Spinner>(R.id.playerOneSpinner)
        val playerTwoSpinner = findViewById<Spinner>(R.id.playerTwoSpinner)
        if (playerOneSpinner != null && playerTwoSpinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, memberNames)
            playerOneSpinner.adapter = adapter
            playerTwoSpinner.adapter = adapter
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

        binding.btnAdd.setOnClickListener() {
            val p1score = binding.playerOneScore.text.toString()
            val p2score = binding.playerTwoScore.text.toString()
            result.result = "$p1score - $p2score"
            if (result.playerOne.isEmpty() || result.playerTwo.isEmpty() || result.result.isEmpty()) {
                Snackbar
                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                app.results.create(result.copy())
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cancel, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}