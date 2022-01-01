package org.wit.freepark.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.freepark.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

const val JSON_FILE = "freeparks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<FreeparkModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class FreeparkJSONStore(private val context: Context) : FreeparkStore {

    var freeparks = mutableListOf<FreeparkModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override suspend fun findAll(): MutableList<FreeparkModel> {
        logAll()
        return freeparks
    }

    override suspend fun create(freepark: FreeparkModel) {
        freepark.id = generateRandomId()
        freeparks.add(freepark)
        serialize()
    }


    override suspend fun update(freepark: FreeparkModel) {
        val freeparksList = findAll() as ArrayList<FreeparkModel>
        var foundFreepark: FreeparkModel? = freeparksList.find { p -> p.id == freepark.id }
        if (foundFreepark != null) {
            foundFreepark.title = freepark.title
            foundFreepark.description = freepark.description
            foundFreepark.image = freepark.image
            foundFreepark.location = freepark.location
        }
        serialize()
    }

    override suspend fun delete(freepark: FreeparkModel){
        val foundFreepark: FreeparkModel? = freeparks.find { it.id == freepark.id }
        freeparks.remove(foundFreepark)
        serialize()
    }
    override suspend fun findById(id:Long) : FreeparkModel? {
        val foundFreepark: FreeparkModel? = freeparks.find { it.id == id }
        return foundFreepark
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(freeparks, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        freeparks = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        freeparks.forEach { Timber.i("$it") }
    }
    override suspend fun clear(){
        freeparks.clear()
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