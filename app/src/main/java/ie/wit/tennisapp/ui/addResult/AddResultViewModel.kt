package ie.wit.tennisapp.ui.addResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.tennisapp.models.ResultModel
import ie.wit.tennisapp.models.ResultsManager

class AddResultViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addResult(result: ResultModel) {
        status.value = try {
            ResultsManager.create(result)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}