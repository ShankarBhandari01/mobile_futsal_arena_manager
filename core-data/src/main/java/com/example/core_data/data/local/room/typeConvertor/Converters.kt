package com.example.core_data.data.local.room.typeConvertor

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }


    @TypeConverter
    fun fromJsonElement(element: JsonElement?): String? {
        return element?.toString()
    }



    @TypeConverter
    fun toJsonElement(value: String): JsonElement {
        return json.parseToJsonElement(value)
    }


    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        return value?.let {
            try {
                json.decodeFromString<List<String>>(it)
            } catch (e: Exception) {
                null
            }
        }
    }


}