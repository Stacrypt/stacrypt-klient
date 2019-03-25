package io.stacrypt.stadroid.data

import com.google.gson.Gson
import java.util.*

//import android.annotation.SuppressLint
//import android.preference.PreferenceManager
//import android.util.Base64
//import com.google.gson.Gson

interface SessionStorage {
    fun load(key: String): String?

    fun save(pair: Pair<String, String?>)
}

class MemoryStorage : SessionStorage {

    private val data: MutableMap<String, String> = HashMap()

    override fun load(key: String): String? = data[key]

    override fun save(pair: Pair<String, String?>) = data.set(pair.first, pair.second ?: "")
}

//@SuppressLint("StaticFieldLeak")
object sessionManager {

    private const val KEY_JWT_TOKE = "jwtToken"
    private const val KEY_FB_TOKE = "fbToken"

    var storage: SessionStorage = MemoryStorage()

    var jwtToken: String? = null
        set(value) {
            field = value
            storage.save(KEY_JWT_TOKE to value)
        }
        get() {
            return storage.load(KEY_JWT_TOKE)
        }

    var fbToken: String? = null
        set(value) {
            field = value
            storage.save(KEY_FB_TOKE to value)
        }
        get() {
            return storage.load(KEY_FB_TOKE)
        }

    fun login(t: String) {
        jwtToken = t
    }

    fun logout() {
        jwtToken = null
//        cookieJar.clear()
//        cookieJar.clearSession()
    }

    fun isLoggedIn() = jwtToken != null

    fun getPayload(): JwtPayload = Gson().fromJson(
        Base64.getDecoder().decode(jwtToken?.split(".")!![1]).toString(Charsets.UTF_8),
        JwtPayload::class.java
    )

    val roles: List<String> get () = getPayload().roles

    val isAdmin: Boolean get() = roles.contains("admin")
    val isClient: Boolean get() = isSemiTrustedClient or roles.contains("client")
    val isSemiTrustedClient: Boolean get() = isTrustedClient or roles.contains("semitrusted_client")
    val isTrustedClient: Boolean get() = roles.contains("trusted_client")

    fun bearerToken(): String? = jwtToken.run { "Bearer ${this}" }
}

data class JwtPayload(val sessionId: String, val memberId: String, val email: String, val roles: List<String>)
