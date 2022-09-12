package com.pi.androidbasehiltmvvm.core.network

import android.content.res.AssetManager
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory

interface SSLPinningApplier {

    val password: String

    fun apply(
        clientBuilder: OkHttpClient.Builder,
        assetManager: AssetManager,
        certificatePath: String
    )

    fun apply(
        clientBuilder: OkHttpClient.Builder,
        assetManager: AssetManager,
        certificatePaths: List<String>
    )

    fun apply(clientBuilder: OkHttpClient.Builder, apiURL: String, publickKey: String)

    fun apply(clientBuilder: OkHttpClient.Builder, apiURL: String, publicKeys: List<String>)

    fun trustAllCertificatesForDebug(clientBuilder: OkHttpClient.Builder)

    fun generateCertificate(inputStream: InputStream): Certificate {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        return certificateFactory.generateCertificate(inputStream)
    }

    fun generateKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, password.toCharArray())
        return keyStore
    }
}
