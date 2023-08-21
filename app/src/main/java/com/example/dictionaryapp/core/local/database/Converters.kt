package com.example.dictionaryapp.core.local.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.dictionaryapp.core.parser.JsonParser
import com.example.dictionaryapp.feature.dictionary.domain.model.Meaning
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {

    @TypeConverter
    fun fromMeaningToJson(json: String): List<Meaning> {
        return jsonParser.fromJson(json, object : TypeToken<ArrayList<Meaning>>() {}.type)
            ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Meaning>): String {
        return jsonParser.toJson(
            meanings,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: ""
    }
}