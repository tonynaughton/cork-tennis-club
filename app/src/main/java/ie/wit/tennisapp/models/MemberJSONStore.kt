package ie.wit.tennisapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.tennisapp.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "members.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<MemberModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class MemberJSONStore(private val context: Context) : MemberStore {

    var members = mutableListOf<MemberModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<MemberModel> {
        logAll()
        return members
    }

    override fun create(member: MemberModel) {
        member.id = generateRandomId()
        members.add(member)
        serialize()
    }


    override fun update(member: MemberModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(members, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        members = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        members.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
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