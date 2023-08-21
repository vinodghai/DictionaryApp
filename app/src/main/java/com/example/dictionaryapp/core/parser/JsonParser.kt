package com.example.dictionaryapp.core.parser

import java.lang.reflect.Type

interface JsonParser {

    fun <T> fromJson(jsonString: String, type: Type): T?

    fun <T> toJson(data: T, type: Type): String?
}