package ie.wit.tennisapp.ui.auth

import android.app.DatePickerDialog
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
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityRegisterBinding
import ie.wit.tennisapp.helpers.showImagePicker
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel
import ie.wit.tennisapp.ui.home.Home
import timber.log.Timber
import timber.log.Timber.i
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity() : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var member = MemberModel()
    lateinit var app: MainApp
    private lateinit var auth: FirebaseAuth
    private lateinit var togglePasswordVisButton: ImageButton
    var edit = false
    var cal: Calendar = Calendar.getInstance()
    private var userDateOfBirth: TextView? = null
    private var buttonAddDate: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        registerImagePickerCallback()

        app = application as MainApp

        val selectExperienceString = getString(R.string.select_experience)
        val experienceOptions = mutableListOf(selectExperienceString, "Beginner", "Intermediate", "Experienced")

        val experienceSpinner = findViewById<Spinner>(R.id.experienceSpinner)
        if (experienceSpinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, experienceOptions)
            experienceSpinner.adapter = adapter
        }

        userDateOfBirth = binding.resultDate
        buttonAddDate = binding.addDateButton

        userDateOfBirth!!.text = "--/--/----"

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
                this,
                R.style.datePickerStyle,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        if (intent.hasExtra("member_edit")) {
            edit = true
            member = intent.extras?.getParcelable("member_edit")!!
            binding.firstName.setText(member.firstName)
            binding.lastName.setText(member.lastName)
            binding.email.setText(member.email)
            binding.password.setText(member.password)
            userDateOfBirth!!.text = member.dateOfBirth
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

        togglePasswordVisButton = findViewById(R.id.togglePasswordVisButton)
        togglePasswordVisButton.setImageResource(R.drawable.ic_eye)

        auth = FirebaseAuth.getInstance()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        userDateOfBirth!!.text = sdf.format(cal.time)
    }

    private fun createAccount(email: String, password: String) {
        Timber.d( "createAccount:$email")
        if (!validateForm()) {
            return
        }

        member.firstName = binding.firstName.text.toString()
        member.lastName = binding.lastName.text.toString()
        member.email = binding.email.text.toString()
        member.password = binding.password.text.toString()
        member.dateOfBirth = userDateOfBirth!!.text.toString()

        if (edit) {
            auth.currentUser!!.updateEmail(member.email)
            auth.currentUser!!.updatePassword(member.password)
            app.members.update(member.copy())
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Timber.d( "createUserWithEmail:success")
                        member.uuid = auth.currentUser!!.uid
                        app.members.create(member.copy())
                        Toast.makeText(baseContext, "Registration successful.",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Home::class.java))
                        setResult(RESULT_OK)
                    } else {
                        Timber.w( "createUserWithEmail:failure $task.exception")
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val firstName = binding.firstName.text.toString()
        if (TextUtils.isEmpty(firstName)) {
            binding.firstName.error = "Required."
            valid = false
        } else {
            binding.lastName.error = null
        }

        val lastName = binding.firstName.text.toString()
        if (TextUtils.isEmpty(lastName)) {
            binding.lastName.error = "Required."
            valid = false
        } else {
            binding.lastName.error = null
        }

        val email = binding.email.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.email.error = "Required."
            valid = false
        } else {
            binding.email.error = null
        }

        val password = binding.password.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.password.error = "Required."
            valid = false
        } else {
            binding.password.error = null
        }

        val dateOfBirth = userDateOfBirth!!.text.toString()
        if (TextUtils.isEmpty(dateOfBirth)) {
            userDateOfBirth!!.error = "Required."
            valid = false
        } else {
            userDateOfBirth!!.error = null
        }

        if (member.experience === getString(R.string.select_experience)) {
            val errorText = binding.experienceSpinner.selectedView as TextView
            errorText.error = "Required"
            errorText.requestFocus()
            valid = false
        }

        return valid
    }

    private fun togglePasswordVisibility() {
        val passwordEntry = findViewById<EditText>(R.id.password)
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
                binding.email.text.toString(),
                binding.password.text.toString()
            )
            R.id.chooseImage -> showImagePicker(imageIntentLauncher)
            R.id.togglePasswordVisButton -> togglePasswordVisibility()
        }
    }
}