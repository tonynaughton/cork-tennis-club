package ie.wit.tennisapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.tennisapp.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val RESULTS_JSON_FILE = "results.json"
val resultsGsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, ResultUriParser())
    .create()
val resultsListType: Type = object : TypeToken<ArrayList<ResultModel>>() {}.type

fun generateRandomResultId(): Long {
    return Random().nextLong()
}

class ResultJSONStore(private val context: Context) : ResultStore {

    private var results = mutableListOf<ResultModel>()

    init {
        if (exists(context, RESULTS_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<ResultModel> {
        logAll()
        return results
    }

    override fun create(result: ResultModel) {
        result.id = generateRandomResultId()
        results.add(result)
        serialize()
    }


    override fun update(result: ResultModel) {
        val currentResult: ResultModel? = results.find { it.id == result.id }
        if (currentResult != null) {
            results[results.indexOf(currentResult)] = result
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = resultsGsonBuilder.toJson(results, resultsListType)
        write(context, RESULTS_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, RESULTS_JSON_FILE)
        results = resultsGsonBuilder.fromJson(jsonString, resultsListType)
    }

    private fun logAll() {
        results.forEach { Timber.i("$it") }
    }
}

class ResultUriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
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