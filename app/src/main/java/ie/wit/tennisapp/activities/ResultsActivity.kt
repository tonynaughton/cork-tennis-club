package ie.wit.tennisapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.wit.tennisapp.databinding.ActivityResultsBinding
import ie.wit.tennisapp.models.MatchModel
import timber.log.Timber
import timber.log.Timber.i

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    var match = MatchModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Results Activity started...")

        binding.btnAdd.setOnClickListener() {
            match.result = binding.matchResult.text.toString()
            if (match.result.isNotEmpty()) {
                i("add Button Pressed: ${match.result}")
            }
            else {
                Snackbar
                    .make(it,"Please enter a result", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}