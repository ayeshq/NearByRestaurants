package com.wolt.nearbyrestaurants.store

import java.lang.reflect.Type

interface DataStore {

    fun <T> storeRecord(key: String, record: T, recordType: Type)

    fun <T> readRecord(key: String, recordType: Type): T?

    fun deleteRecord(key: String)
}
