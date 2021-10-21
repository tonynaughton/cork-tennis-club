package ie.wit.tennisapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityAddMemberBinding
import ie.wit.tennisapp.helpers.showImagePicker
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel
import timber.log.Timber.i

class AddMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMemberBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var member = MemberModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityAddMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        registerImagePickerCallback()

        app = application as MainApp

        if (intent.hasExtra("member_edit")) {
            edit = true
            member = intent.extras?.getParcelable("member_edit")!!
            binding.memberFirstName.setText(member.firstName)
            binding.memberLastName.setText(member.lastName)
            binding.memberAge.setText(member.age)
            binding.btnAdd.setText(R.string.save_result)
            Picasso.get()
                .load(member.image)
                .into(binding.memberImage)
            if (member.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_member_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            member.firstName = binding.memberFirstName.text.toString()
            member.lastName = binding.memberLastName.text.toString()
            member.age = binding.memberAge.text.toString()
            if (member.firstName.isEmpty() || member.lastName.isEmpty() || member.age.isEmpty()) {
                Snackbar
                    .make(it, R.string.fill_in_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.members.update(member.copy())
                } else {
                    app.members.create(member.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
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
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}