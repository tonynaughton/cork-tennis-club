package ie.wit.tennisapp.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
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
        var edit = false
        binding = ActivityAddResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        val allMembers = app.members.findAll()
        var memberNames: MutableList<String> = allMembers.map{it.firstName + " " + it.lastName} as MutableList<String>

        val playerOneSpinner = findViewById<Spinner>(R.id.playerOneSpinner)
        val playerTwoSpinner = findViewById<Spinner>(R.id.playerTwoSpinner)
        if (playerOneSpinner != null && playerTwoSpinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, memberNames)
            playerOneSpinner.adapter = adapter
            playerTwoSpinner.adapter = adapter
        }

        if (intent.hasExtra("result_edit")) {
            edit = true
            result = intent.extras?.getParcelable("result_edit")!!
            println("NAME POSITION: " + memberNames.indexOf(result.playerOne))
            binding.playerOneSpinner.setSelection(memberNames.indexOf(result.playerOne))
            binding.playerTwoSpinner.setSelection(memberNames.indexOf(result.playerTwo))
            var scores = result.score.split("-")
            binding.playerOneScore.setText(scores[0])
            binding.playerTwoScore.setText(scores[1])
            binding.btnAdd.setText(R.string.save_result)
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
            result.score = "$p1score-$p2score"
            if (result.playerOne.isEmpty() || result.playerTwo.isEmpty() || result.score.isEmpty()) {
                Snackbar
                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.results.update(result.copy())
                } else {
                    app.results.create(result.copy())
                }
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