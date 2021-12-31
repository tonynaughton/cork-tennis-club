package ie.wit.tennisapp.ui.results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.tennisapp.models.ResultModel
import ie.wit.tennisapp.models.ResultsManager

class ResultsViewModel: ViewModel() {

    private val resultsList = MutableLiveData<List<ResultModel>>()

    val observableResultsList: LiveData<List<ResultModel>>
        get() = resultsList

    init {
        load()
    }

    fun load() {
        resultsList.value = ResultsManager.findAll()
    }
}