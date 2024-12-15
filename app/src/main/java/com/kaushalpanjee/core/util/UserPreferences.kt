package com.kaushalpanjee.core.util

import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kaushal_panjee")


private const val ACCESS_TOKEN = "ACCESS_TOKEN"
private const val REFRESH_TOKEN = "REFRESH_TOKEN"
private const val IS_LOGGED_IN = "IS_LOGGED_IN"
private const val USER_EMAIL = "USER_EMAIL"
private const val USER_PHONE_NUMBER = "USER_PHONE_NUMBER"
private const val USER_ID = "USER_ID"
private const val USER_TYPE = "USER_TYPE"
private const val USER_NAME = "USER_NAME"
private const val USER_IMAGE = "USER_IMAGE"
private const val USER_ROLE_ID = "USER_ROLE_ID"
private const val IS_REGISTERED = "IS_REGISTERED"
private const val SELECTED_SCHEME_CODE = "SELECTED_SCHEME_CODE"


class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    val appContext = context.applicationContext
    val gson = Gson()
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    suspend fun saveStringDataToDataStore(key: String, data: String) {
        log("DATA-->", "saveStringDataToDataStore")
        val key = stringPreferencesKey(key)
        appContext.dataStore.edit {
            it[key] = data
        }
    }

    fun getStringDataFromDataStore(key: String): String {
        log("DATA-->", "getStringDataFromDataStore")
        val key = stringPreferencesKey(key)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value;
    }

    suspend fun saveIntDataToDataStore(key: String, data: Int) {
        val key = intPreferencesKey(key)
        appContext.dataStore.edit {
            it[key] = data
        }
    }

    fun getIntDataFromDataStore(key: String): Int {
        val key = intPreferencesKey(key)
        var value = 0
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: 0
            }.first()
        }
        return value;

    }

    suspend fun saveLongDataToDataStore(key: String, data: Long) {
        val key = longPreferencesKey(key)
        appContext.dataStore.edit {
            it[key] = data
        }
    }

    fun getLongDataFromDataStore(key: String): Long {
        val key = longPreferencesKey(key)
        var value = 0L
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: 0
            }.first()
        }
        return value;

    }


    suspend fun saveBoolDataToDataStore(key: String, data: Boolean) {
        val key = booleanPreferencesKey(key)
        appContext.dataStore.edit {
            it[key] = data
        }
    }

    fun getBooleanDataFromDataStore(key: String): Boolean {
        val key = booleanPreferencesKey(key)
        var value = false
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: false
            }.first()
        }
        return value;
    }

    suspend fun <T> saveObjectDataToDataStore(key: String, data: T) {
        // val dataObject = ObjectWrapperClass(data)
        val dataAsString = gson.toJson(data)
        val key = stringPreferencesKey(key)
        appContext.dataStore.edit {
            it[key] = dataAsString
        }

    }

    inline fun <reified T> getObjectDataToStore(key: String): T {
        val key = stringPreferencesKey(key)
        var value: T
        runBlocking {
            value = appContext.dataStore.data.map {
                gson.fromJson(it[key], T::class.java)
            }.first()
        }
        return value
    }

     suspend fun clear() {
        appContext.dataStore.edit {
            it.clear()
        }
    }




    fun saveAccessToken(accessToken: String) {
        val key = stringPreferencesKey(ACCESS_TOKEN)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = accessToken
            }
        }
    }

    fun getAccessToken(): String {
        val key = stringPreferencesKey(ACCESS_TOKEN)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }

    fun saveRefreshToken(refreshToken: String) {
        val key = stringPreferencesKey(REFRESH_TOKEN)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = refreshToken
            }
        }
    }

    fun getRefreshToken(): String {
        val key = stringPreferencesKey(REFRESH_TOKEN)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }

    fun setLoggedIn(isUserLoggedIn:Boolean) {
        val key = booleanPreferencesKey(IS_LOGGED_IN)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = isUserLoggedIn
            }
        }
    }

    fun getLoggedIn(): Boolean {
        val key = booleanPreferencesKey(IS_LOGGED_IN)
        var value = false
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: false
            }.first()
        }
        return value
    }


    fun saveUserEmail(userEmail: String) {
        val key = stringPreferencesKey(USER_EMAIL)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = userEmail
            }
        }
    }

    fun getUserEmail(): String {
        val key = stringPreferencesKey(USER_EMAIL)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }


    fun saveUserPhoneNumber(userNumber: String) {
        val key = stringPreferencesKey(USER_PHONE_NUMBER)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = userNumber
            }
        }
    }

    fun getUserPhoneNumber(): String {
        val key = stringPreferencesKey(USER_PHONE_NUMBER)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }

    fun saveUserID(userId: String) {
        val key = stringPreferencesKey(USER_ID)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = userId
            }
        }
    }

    fun getUseID(): String {
        val key = stringPreferencesKey(USER_ID)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }


    fun saveUserType(userId: String) {
        val key = stringPreferencesKey(USER_TYPE)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = userId
            }
        }
    }

    fun getUserType(): String {
        val key = stringPreferencesKey(USER_TYPE)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }

    fun saveUserName(userName: String) {
        val key = stringPreferencesKey(USER_NAME)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = userName
            }
        }
    }

    fun getUserName(): String {
        val key = stringPreferencesKey(USER_NAME)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }

    fun saveUserImage(userImage: String) {
        val key = stringPreferencesKey(USER_IMAGE)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = userImage
            }
        }
    }

    fun getUserImage(): String {
        val key = stringPreferencesKey(USER_IMAGE)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }


    fun saveUserRoleId(roleId: Int) {
        val key = intPreferencesKey(USER_ROLE_ID)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = roleId
            }
        }
    }

    fun getUserRoleId(): Int {
        val key = intPreferencesKey(USER_ROLE_ID)
        var value = 0
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: 0
            }.first()
        }
        return value

    }


    fun setIsRegistered(isRegistered:Boolean) {
        val key = booleanPreferencesKey(IS_REGISTERED)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = isRegistered
            }
        }
    }

    fun getIsRegistered(): Boolean {
        val key = booleanPreferencesKey(IS_REGISTERED)
        var value = false
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: false
            }.first()
        }
        return value
    }

    fun saveSchemeCode(userImage: String) {
        val key = stringPreferencesKey(SELECTED_SCHEME_CODE)
        coroutineScope.launch {
            appContext.dataStore.edit {
                it[key] = userImage
            }
        }
    }

    fun getSchemeCode(): String {
        val key = stringPreferencesKey(SELECTED_SCHEME_CODE)
        var value = ""
        runBlocking {
            value = appContext.dataStore.data.map {
                it[key] ?: ""
            }.first()
        }
        return value
    }

    suspend fun logout(context: Context){


       /* val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)*/

    }





}
