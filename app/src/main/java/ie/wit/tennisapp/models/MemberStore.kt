package ie.wit.tennisapp.models

interface MemberStore {
    fun findAll(): List<MemberModel>
    fun create(result: MemberModel)
    fun update(result: MemberModel)
    fun delete(result: MemberModel)
}