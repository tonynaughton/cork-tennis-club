package ie.wit.tennisapp.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the contact fragment"
    }
    val text: LiveData<String> = _text
}