package criptocatbox.http.client

import okhttp3.OkHttpClient

class OkHttpClientSingleton {
    companion object {
        val HTTP_CLIENT = OkHttpClient()
    }
}