package com.pi.androidbasehiltmvvm.core.network

import android.content.res.AssetManager
import android.net.Uri
import android.text.TextUtils
import com.pi.androidbasehiltmvvm.BuildConfig
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.FileNotFoundException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class APISSLPinningApplier : SSLPinningApplier {

    override val password: String = "password"

    override fun apply(
        clientBuilder: OkHttpClient.Builder,
        assetManager: AssetManager,
        certificatePath: String
    ) {
        apply(clientBuilder, assetManager, listOf(certificatePath))
    }

    override fun apply(
        clientBuilder: OkHttpClient.Builder,
        assetManager: AssetManager,
        certificatePaths: List<String>
    ) {
        if (certificatePaths.isEmpty()) {
            return
        }
        try {
            // Specify a password. Any password will work
            val password = password.toCharArray()

            // Put the Certificate into a KeyStore
            val keyStore = generateKeyStore()

            // Read Certificate from assets
            certificatePaths.forEachIndexed { index, certificatePath ->
                // Read Certificate from assets
                val inputStream = assetManager.open(certificatePath)

                // Create Certificate from input stream
                val certificate = generateCertificate(inputStream)

                keyStore.setCertificateEntry(index.toString(), certificate)
            }

            // Use it to build an X509 TrustManager
            val keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(keyStore, password)

            // Create and init TrustManagerFactory
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            // Create and init SSLContext
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(
                keyManagerFactory.keyManagers,
                trustManagerFactory.trustManagers,
                SecureRandom()
            )

            // Add to the OkHttpClient
            clientBuilder.sslSocketFactory(
                sslContext.socketFactory,
                trustManagerFactory.trustManagers[0] as X509TrustManager
            )
        } catch (e: FileNotFoundException) {
            Timber.d("Certificate did not add for %s", BuildConfig.FLAVOR)
        } catch (e: Exception) {
            Timber.w(e)
        }
    }

    override fun apply(clientBuilder: OkHttpClient.Builder, apiURL: String, publickKey: String) {
        apply(clientBuilder, apiURL, listOf(publickKey))
    }

    override fun apply(
        clientBuilder: OkHttpClient.Builder,
        apiURL: String,
        publicKeys: List<String>
    ) {
        if (publicKeys.isEmpty()) {
            return
        }

        val uri = Uri.parse(apiURL)
        val certificateBuilder = CertificatePinner.Builder()
        publicKeys.forEach { key ->
            uri.host?.let { uri ->
                certificateBuilder.add(uri, String.format("sha256/%s", key))
            }
        }

        val certificatePinner = certificateBuilder.build()
        clientBuilder.certificatePinner(certificatePinner)
    }

    override fun trustAllCertificatesForDebug(clientBuilder: OkHttpClient.Builder) {
        if (!BuildConfig.DEBUG) {
            return
        }
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                    // no-op
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                    // no-op
                }

                override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            clientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            clientBuilder.hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            Timber.w(e)
        }
    }

    fun getSSLPublicKeys(): List<String> {
        if (BuildConfig.DEBUG) return emptyList()
        if (TextUtils.isEmpty(BuildConfig.SERVICE_PUBLIC_KEY)) return emptyList()
        return BuildConfig.SERVICE_PUBLIC_KEY.split(",")
    }
}
