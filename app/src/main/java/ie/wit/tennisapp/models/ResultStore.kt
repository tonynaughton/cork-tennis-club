package ie.wit.tennisapp.models

interface ResultStore {
    fun findAll(): List<MatchModel>
    fun create(result: MatchModel)
    fun update(result: MatchModel)
}