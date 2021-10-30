package ie.wit.tennisapp.activities

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityLoginBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel

class LoginActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var member = MemberModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        val togglePasswordVisButton = findViewById<ImageButton>(R.id.togglePasswordVisibility)
        togglePasswordVisButton.setImageResource(R.drawable.ic_eye)

        binding.togglePasswordVisibility.setOnClickListener() {
            val passwordEntry = findViewById<EditText>(R.id.memberPassword)
            if(togglePasswordVisButton.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.ic_eye)?.constantState) {
                togglePasswordVisButton.setImageResource(R.drawable.ic_eye_slash)
                passwordEntry.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                togglePasswordVisButton.setImageResource(R.drawable.ic_eye)
                passwordEntry.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.btnAdd.setOnClickListener() {
            member.email = binding.memberEmail.text.toString()
            member.password = binding.memberPassword.text.toString()
            if (member.email.isEmpty() || member.password.isEmpty()) {
                Snackbar
                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                var verifiedUser = false
                var allMembers = app.members.findAll()
                allMembers.forEach { it ->
                    if ((it.email == member.email) && (it.password == member.password)) {
                        verifiedUser = true
                    }
                }
                if (verifiedUser) {
                    setResult(RESULT_OK)
                    startActivity(Intent(this, ListResultsActivity::class.java))
                } else {
                    Snackbar
                        .make(it, R.string.user_does_not_exist, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
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

