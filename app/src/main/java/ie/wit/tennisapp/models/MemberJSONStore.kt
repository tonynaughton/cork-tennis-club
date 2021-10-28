package ie.wit.tennisapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.tennisapp.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val MEMBERS_JSON_FILE = "members.json"
val membersGsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, MemberUriParser())
    .create()
val membersListType: Type = object : TypeToken<ArrayList<MemberModel>>() {}.type

fun generateRandomMemberId(): Long {
    return Random().nextLong()
}

class MemberJSONStore(private val context: Context) : MemberStore {

    private var members = mutableListOf<MemberModel>()

    init {
        if (exists(context, MEMBERS_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<MemberModel> {
        logAll()
        return members
    }

    override fun create(member: MemberModel) {
        member.id = generateRandomMemberId()
        members.add(member)
        serialize()
    }


    override fun update(member: MemberModel) {
        val currentMember: MemberModel? = members.find { it.id == member.id }
        if (currentMember != null) {
            members[members.indexOf(currentMember)] = member
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = membersGsonBuilder.toJson(members, membersListType)
        write(context, MEMBERS_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, MEMBERS_JSON_FILE)
        members = membersGsonBuilder.fromJson(jsonString, membersListType)
    }

    private fun logAll() {
        members.forEach { Timber.i("$it") }
    }
}

class MemberUriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}