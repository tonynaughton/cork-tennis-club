package ie.wit.tennisapp.activities

import android.app.AlertDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityLoginBinding
import ie.wit.tennisapp.fragments.ResultsFragment
import ie.wit.tennisapp.helpers.showImagePicker
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel
import timber.log.Timber

class LoginActivity() : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    var member = MemberModel()
    lateinit var app: MainApp
    private lateinit var auth: FirebaseAuth
    private lateinit var togglePasswordVisButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        binding.loginButton.setOnClickListener(this)
        binding.togglePasswordVisButton.setOnClickListener(this)

//        binding.loginButton.setOnClickListener() {
//            member.email = binding.memberEmail.text.toString()
//            member.password = binding.memberPassword.text.toString()
//            if (member.email.isEmpty() || member.password.isEmpty()) {
//                Snackbar
//                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
//                    .show()
//            } else {
//                var verifiedUser = false
//                var allMembers = app.members.findAll()
//                allMembers.forEach { it ->
//                    if ((it.email == member.email) && (it.password == member.password)) {
//                        verifiedUser = true
//                    }
//                }
//                if (verifiedUser) {
//                    setResult(RESULT_OK)
//                    startActivity(Intent(this, ResultsFragment::class.java))
//                } else {
//                    Snackbar
//                        .make(it, R.string.user_does_not_exist, Snackbar.LENGTH_LONG)
//                        .show()
//                }
//            }
//        }

        togglePasswordVisButton = findViewById(R.id.togglePasswordVisButton)
        togglePasswordVisButton.setImageResource(R.drawable.ic_eye)

        auth = FirebaseAuth.getInstance()
    }

    private fun logIn(email: String, password: String) {
        Timber.d( "signIn:$email")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d( "signInWithEmail:success")
                    val user = auth.currentUser
                    setResult(RESULT_OK)
                    startActivity(Intent(this, Home::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w( "signInWithEmail:failure $task.exception")
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                if (!task.isSuccessful) {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.memberEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.memberEmail.error = "Required."
            valid = false
        } else {
            binding.memberEmail.error = null
        }

        val password = binding.memberPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.memberPassword.error = "Required."
            valid = false
        } else {
            binding.memberPassword.error = null
        }

        return valid
    }

    private fun togglePasswordVisibility() {
        val passwordEntry = findViewById<EditText>(R.id.memberPassword)
        if(togglePasswordVisButton.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.ic_eye)?.constantState) {
            togglePasswordVisButton.setImageResource(R.drawable.ic_eye_slash)
            passwordEntry.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            togglePasswordVisButton.setImageResource(R.drawable.ic_eye)
            passwordEntry.transformationMethod = PasswordTransformationMethod.getInstance()
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> logIn(binding.memberEmail.text.toString(), binding.memberPassword.text.toString())
            R.id.togglePasswordVisButton -> togglePasswordVisibility()
        }
    }
}

