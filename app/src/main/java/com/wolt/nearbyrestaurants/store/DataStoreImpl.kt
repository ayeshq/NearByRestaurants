package com.wolt.nearbyrestaurants.store

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import javax.inject.Inject

class DataStoreImpl @Inject constructor(
    @ApplicationContext context: Context
) : DataStore {

    private val preferences: SharedPreferences = context.getSharedPreferences(DataStore::class.java.getName(), Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun <T> storeRecord(key: String, record: T, recordType: Type) {
        val json = gson.toJson(record, recordType)
        preferences.edit()
            .putString(key, json)
            .apply()
    }

    override fun <T> readRecord(key: String, recordType: Type): T? {
        val json = preferences.getString(key, "")
        return if (TextUtils.isEmpty(json)) {
            null
        } else try {
            gson.fromJson<T>(json, recordType)
        } catch (ignored: Exception) {
            null
        }
    }

    override fun deleteRecord(key: String) {
        preferences.edit()
            .remove(key)
            .apply()
    }
}
