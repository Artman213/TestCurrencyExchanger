package com.example.testcurrencyexchanger.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.testcurrencyexchanger.module.ExchangeRatesResponse
import com.example.testcurrencyexchanger.module.UserMoney
import com.google.gson.Gson
import javax.inject.Singleton


private const val SHARED_PREFS_FILE_NAME = "sharedPrefs"
private const val KEY_USER_MONEY = "KEY_USER_MONEY"
private const val KEY_NEED_FEE = "KEY_NEED_FEE"
private const val KEY_LOGIC_FEE = "KEY_LOGIC_FEE"

@Singleton
class SecureRepository(private val appContext: Context) {
    private val masterKeyAlias: String by lazy { generateMasterKey() }
    private val sharedPreferences: SharedPreferences by lazy { getEncryptedSharedPreferences() }

    private fun generateMasterKey(): String {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        return MasterKeys.getOrCreate(keyGenParameterSpec)
    }

    private fun getEncryptedSharedPreferences(): SharedPreferences {
        val sharedPrefsFile = SHARED_PREFS_FILE_NAME
        return EncryptedSharedPreferences.create(
            sharedPrefsFile,
            masterKeyAlias,
            appContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private var needFee: Boolean
        get() {
            return sharedPreferences.getBoolean(KEY_NEED_FEE, false)
        }
        set(value) {
            with(sharedPreferences.edit()) {
                putBoolean(KEY_NEED_FEE, value)
                this.apply()
            }
        }
    private var countFreeFee: Int
        get() {
            return sharedPreferences.getInt(KEY_LOGIC_FEE, 0)
        }
        set(value) {
            with(sharedPreferences.edit()) {
                putInt(KEY_LOGIC_FEE, value)
                this.apply()
            }
        }
    private var userMoney: String?
        get() {
            return sharedPreferences.getString(KEY_USER_MONEY, null)
        }
        set(value) {
            with(sharedPreferences.edit()) {
                putString(KEY_USER_MONEY, value)
                this.apply()
            }
        }

    fun getUserMoney(): UserMoney? {
        if (userMoney == null)
            return null
        val gson = Gson()
        val json: String = userMoney as String
        return gson.fromJson(json, UserMoney::class.java)
    }

    fun setUserMoneyObject(rates: HashMap<String, Double>) {
        val gson = Gson()
        val userMoneyObject = UserMoney()
        userMoneyObject.userMoneyHashMap["EUR"] = 1000.0
        for (exchange in rates) {
            if (exchange.key != "EUR")
                userMoneyObject.userMoneyHashMap[exchange.key] = 0.0
        }
        val json: String = gson.toJson(userMoneyObject)
        userMoney = json

    }
    fun updateUserMoney(userMoney: UserMoney){
        val gson = Gson()
        val json: String = gson.toJson(userMoney)
        this.userMoney = json
    }

    fun checkFee(): Boolean {
        if (!needFee) {
            if (countFreeFee == 5)
                needFee = true
            else
                countFreeFee += 1
        }
        return needFee
    }

}