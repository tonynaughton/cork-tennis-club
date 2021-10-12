package ie.wit.tennisapp.models

interface ResultStore {
    fun findAll(): List<MatchModel>
    fun create(placemark: MatchModel)
}