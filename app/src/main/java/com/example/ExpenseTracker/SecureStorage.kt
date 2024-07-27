package com.example.ExpenseTracker

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import java.security.KeyStore
import java.security.UnrecoverableKeyException

class SecureStorage(private val context: Context) {

    companion object {
        private const val KEY_ALIAS = "my_key_alias"
        private const val SHARED_PREFS_NAME = "secure_prefs"
        private const val ENCRYPTED_PIN_KEY = "encrypted_pin"
        private const val IV_KEY = "iv"
        private const val TAG = "SecureStorage"
    }

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        try {
            load(null)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load Keystore", e)
            throw RuntimeException("Failed to load Keystore", e)
        }
    }

    private fun isKeyGenerated(): Boolean {
        return keyStore.containsAlias(KEY_ALIAS)
    }

    @Throws(Exception::class)
    private fun generateKey() {
        if (isKeyGenerated())
        {
            Log.d(TAG, "Key already generated")
            return
        }

        try {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
            Log.d(TAG, "Key generated successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate key", e)
            throw RuntimeException("Failed to generate key", e)
        }
    }

    @Throws(Exception::class)
    private fun getSecretKey(): SecretKey {
        return try {
            keyStore.getKey(KEY_ALIAS, null) as SecretKey
        } catch (e: UnrecoverableKeyException) {
            Log.e(TAG, "Failed to retrieve key", e)
            throw RuntimeException("Failed to retrieve key", e)
        }
    }

    @Throws(Exception::class)
    private fun getCipher(): Cipher {
        return try {
            Cipher.getInstance("AES/GCM/NoPadding")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get Cipher instance", e)
            throw RuntimeException("Failed to get Cipher instance", e)
        }
    }

    fun savePin(pin: String) {
        try {
            generateKey()

            val cipher = getCipher()
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

            val iv = cipher.iv
            val encryptedPin = cipher.doFinal(pin.toByteArray(Charsets.UTF_8))

            val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                putString(ENCRYPTED_PIN_KEY, Base64.encodeToString(encryptedPin, Base64.DEFAULT))
                putString(IV_KEY, Base64.encodeToString(iv, Base64.DEFAULT))
                apply()
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to save PIN", e)
        }
    }

    fun verifyPin(pin: String): Boolean {
        return try {
            val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            val encryptedPinStr = prefs.getString(ENCRYPTED_PIN_KEY, null)
            val ivStr = prefs.getString(IV_KEY, null)

            if (encryptedPinStr == null || ivStr == null) {
                return false
            }

            val encryptedPin = Base64.decode(encryptedPinStr, Base64.DEFAULT)
            val iv = Base64.decode(ivStr, Base64.DEFAULT)

            val cipher = getCipher()
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))

            val decryptedPin = cipher.doFinal(encryptedPin)
            pin.toByteArray(Charsets.UTF_8).contentEquals(decryptedPin)
        } catch (e: Exception) {
            throw RuntimeException("Failed to verify PIN", e)
        }
    }
    fun isPinSaved(): Boolean {
        val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val encryptedPinStr = prefs.getString(ENCRYPTED_PIN_KEY, null)
        val ivStr = prefs.getString(IV_KEY, null)
        return encryptedPinStr != null && ivStr != null
    }
}
