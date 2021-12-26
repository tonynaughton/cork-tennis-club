package ie.wit.tennisapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityRegisterBinding
import ie.wit.tennisapp.helpers.showImagePicker
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel
import timber.log.Timber
import timber.log.Timber.i

class RegisterActivity() : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var member = MemberModel()
    lateinit var app: MainApp
    private lateinit var auth: FirebaseAuth
    private lateinit var togglePasswordVisButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        registerImagePickerCallback()

        app = application as MainApp

        val selectExperienceString = "Experience:"
        val experienceOptions = mutableListOf(selectExperienceString, "Beginner", "Intermediate", "Experienced")

        val experienceSpinner = findViewById<Spinner>(R.id.experienceSpinner)
        if (experienceSpinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, experienceOptions)
            experienceSpinner.adapter = adapter
        }

        if (intent.hasExtra("member_edit")) {
            edit = true
            member = intent.extras?.getParcelable("member_edit")!!
            binding.memberFirstName.setText(member.firstName)
            binding.memberLastName.setText(member.lastName)
            binding.memberEmail.setText(member.email)
            binding.memberPassword.setText(member.password)
            binding.memberDob.setText(member.dob)
            binding.experienceSpinner.setSelection(experienceOptions.indexOf(member.experience))
            binding.registerButton.setText(R.string.update_member)
            Picasso.get()
                .load(member.image)
                .into(binding.memberImage)
            if (member.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_member_image)
            }
        }

        experienceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                member.experience = experienceOptions[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.registerButton.setOnClickListener(this)
        binding.togglePasswordVisButton.setOnClickListener(this)
        binding.chooseImage.setOnClickListener(this)

//        binding.registerButton.setOnClickListener() {
//            member.firstName = binding.memberFirstName.text.toString()
//            member.lastName = binding.memberLastName.text.toString()
//            member.email = binding.memberEmail.text.toString()
//            member.password = binding.memberPassword.text.toString()
//            member.dob = binding.memberDob.text.toString()
//            if (member.firstName.isEmpty() || member.lastName.isEmpty()
//                || member.dob.isEmpty() || member.experience == selectExperienceString
//                || member.email.isEmpty() || member.password.isEmpty()) {
//                Snackbar
//                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
//                    .show()
//            } else {
//                if (edit) {
//                    app.members.update(member.copy())
//                } else {
//                    app.members.create(member.copy())
//                }
//                setResult(RESULT_OK)
//                finish()
//            }
//        }

        togglePasswordVisButton = findViewById(R.id.togglePasswordVisButton)
        togglePasswordVisButton.setImageResource(R.drawable.ic_eye)

        auth = FirebaseAuth.getInstance()
    }

    private fun createAccount(email: String, password: String) {
        Timber.d( "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d( "createUserWithEmail:success")
                    finish()
                    setResult(RESULT_OK)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w( "createUserWithEmail:failure $task.exception")
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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            member.image = result.data!!.data!!
                            Picasso.get()
                                .load(member.image)
                                .into(binding.memberImage)
                            binding.chooseImage.setText(R.string.change_member_image)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.registerButton -> createAccount(
                binding.memberEmail.text.toString(),
                binding.memberPassword.text.toString()
            )
            R.id.chooseImage -> showImagePicker(imageIntentLauncher)
            R.id.togglePasswordVisButton -> togglePasswordVisibility()
        }
    }
}