package com.example.myapplication.myapplication.flashcall.utils

import com.google.firebase.Timestamp
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.* // For Gson serialization/deserialization
import java.lang.reflect.Type
import java.util.* // For Date

class TimestampConverter : JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    override fun serialize(
        src: Timestamp?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return if (src == null) JsonNull.INSTANCE
        else JsonPrimitive(src.toDate().time)
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Timestamp {
        return if (json == null || json.isJsonNull) {
            Timestamp(Date()) // Default to current time if null
        } else {
            Timestamp(Date(json.asJsonPrimitive.asLong))
        }
    }
}
