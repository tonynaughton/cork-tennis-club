package ie.wit.tennisapp.models

import timber.log.Timber.i



class MembersMemStore : MemberStore {

    var lastId = 0L

    private fun getId(): Long {
        return lastId++
    }

    private val results = ArrayList<MemberModel>()

    override fun findAll(): List<MemberModel> {
        return results
    }

    override fun create(member: MemberModel) {
        member.id = getId()
        results.add(member)
        logAll()
    }

    override fun update(member: MemberModel) {
        var foundMember: MemberModel? = results.find { p -> p.id == member.id }
        println(foundMember)
        if (foundMember != null) {
            foundMember.firstName = member.firstName
            foundMember.lastName = member.lastName
            foundMember.age = member.age
            logAll()
        }
    }

    private fun logAll() {
        results.forEach{ i("$it") }
    }
}