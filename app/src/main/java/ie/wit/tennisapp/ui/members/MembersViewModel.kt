package ie.wit.tennisapp.ui.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MembersViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the members fragment"
    }
    val text: LiveData<String> = _text
}