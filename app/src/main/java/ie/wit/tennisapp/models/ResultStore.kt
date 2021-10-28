package ie.wit.tennisapp.models

interface ResultStore {
    fun findAll(): List<ResultModel>
    fun create(result: ResultModel)
    fun update(result: ResultModel)
}