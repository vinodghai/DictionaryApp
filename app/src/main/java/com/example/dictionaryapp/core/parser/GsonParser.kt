package com.example.dictionaryapp.core.parser

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonParser(
    private val gson: Gson
) : JsonParser {
    override fun <T> fromJson(jsonString: String, type: Type): T? {
        return gson.fromJson(jsonString, type)
    }

    override fun <T> toJson(data: T, type: Type): String? {
        return gson.toJson(data, type)
    }
}