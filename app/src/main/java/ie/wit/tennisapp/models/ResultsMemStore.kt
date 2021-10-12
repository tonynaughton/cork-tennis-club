package ie.wit.tennisapp.models

import timber.log.Timber.i

class ResultsMemStore : ResultStore {

    private val results = ArrayList<MatchModel>()

    override fun findAll(): List<MatchModel> {
        return results
    }

    override fun create(result: MatchModel) {
        results.add(result)
        logAll()
    }

    fun logAll() {
        results.forEach{ i("$it") }
    }
}