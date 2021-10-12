package ie.wit.tennisapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.wit.tennisapp.databinding.ActivityAddResultBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MatchModel
import timber.log.Timber.i

class AddResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddResultBinding
    var match = MatchModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        binding.btnAdd.setOnClickListener() {
            match.playerOne = binding.matchPlayerOne.text.toString()
            match.playerTwo = binding.matchPlayerTwo.text.toString()
            match.result = binding.matchResult.text.toString()
            if (match.playerOne.isNotEmpty() && match.playerTwo.isNotEmpty() && match.result.isNotEmpty()) {
                app.matches.add((match.copy()))
                i("add Button Pressed: $match")
                for (i in app.matches.indices)
                    { i("Match [$i]:${this.app.matches[i]}") }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please fill in all fields", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}