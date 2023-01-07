package com.pi.data.persistence.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.PreferencesMapCompat
import androidx.datastore.preferences.PreferencesProto
import androidx.datastore.preferences.core.*
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<Preferences> {
    override val defaultValue: Preferences
        get() = emptyPreferences()

    override suspend fun readFrom(input: InputStream): Preferences {
        val preferencesProto = PreferencesMapCompat.readFrom(input)

        val mutablePreferences = mutablePreferencesOf()

        preferencesProto.preferencesMap.forEach { (name, value) ->
            addProtoEntryToPreferences(name, value, mutablePreferences)
        }

        return mutablePreferences.toPreferences()
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        val preferences = t.asMap()
        val protoBuilder = PreferencesProto.PreferenceMap.newBuilder()

        for ((key, value) in preferences) {
            protoBuilder.putPreferences(key.name, getValueProto(value))
        }

        protoBuilder.build().writeTo(output)
    }

    private fun getValueProto(value: Any): PreferencesProto.Value {
        return when (value) {
            is Boolean -> PreferencesProto.Value.newBuilder().setBoolean(value).build()
            is Float -> PreferencesProto.Value.newBuilder().setFloat(value).build()
            is Double -> PreferencesProto.Value.newBuilder().setDouble(value).build()
            is Int -> PreferencesProto.Value.newBuilder().setInteger(value).build()
            is Long -> PreferencesProto.Value.newBuilder().setLong(value).build()
            is String -> PreferencesProto.Value.newBuilder().setString(value).build()
            is Set<*> ->
                @Suppress("UNCHECKED_CAST")
                PreferencesProto.Value.newBuilder().setStringSet(
                    PreferencesProto.StringSet.newBuilder().addAllStrings(value as Set<String>)
                ).build()
            else -> throw IllegalStateException(
                "PreferencesSerializer does not support type: ${value.javaClass.name}"
            )
        }
    }

    private fun addProtoEntryToPreferences(
        name: String,
        value: PreferencesProto.Value,
        mutablePreferences: MutablePreferences
    ) {
        return when (value.valueCase) {
            PreferencesProto.Value.ValueCase.BOOLEAN ->
                mutablePreferences[booleanPreferencesKey(name)] =
                    value.boolean
            PreferencesProto.Value.ValueCase.FLOAT -> mutablePreferences[floatPreferencesKey(name)] =
                value.float
            PreferencesProto.Value.ValueCase.DOUBLE -> mutablePreferences[doublePreferencesKey(name)] =
                value.double
            PreferencesProto.Value.ValueCase.INTEGER -> mutablePreferences[intPreferencesKey(name)] =
                value.integer
            PreferencesProto.Value.ValueCase.LONG -> mutablePreferences[longPreferencesKey(name)] =
                value.long
            PreferencesProto.Value.ValueCase.STRING -> mutablePreferences[stringPreferencesKey(name)] =
                value.string
            PreferencesProto.Value.ValueCase.STRING_SET ->
                mutablePreferences[stringSetPreferencesKey(name)] =
                    value.stringSet.stringsList.toSet()
            PreferencesProto.Value.ValueCase.VALUE_NOT_SET ->
                throw CorruptionException("Value not set.")
            null -> throw CorruptionException("Value case is null.")
        }
    }
}