package ie.wit.tennisapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityAddResultBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MatchModel

class AddResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddResultBinding
    var match = MatchModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityAddResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        if (intent.hasExtra("result_edit")) {
            edit = true
            match = intent.extras?.getParcelable("result_edit")!!
            binding.matchPlayerOne.setText(match.playerOne)
            binding.matchPlayerTwo.setText(match.playerTwo)
            binding.matchResult.setText(match.result)
            binding.btnAdd.setText(R.string.save_result)
        }

        binding.btnAdd.setOnClickListener() {
            match.playerOne = binding.matchPlayerOne.text.toString()
            match.playerTwo = binding.matchPlayerTwo.text.toString()
            match.result = binding.matchResult.text.toString()
            if (match.playerOne.isEmpty() || match.playerTwo.isEmpty() || match.result.isEmpty()) {
                Snackbar
                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.matches.update(match.copy())
                } else {
                    app.matches.create(match.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_result, menu)
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