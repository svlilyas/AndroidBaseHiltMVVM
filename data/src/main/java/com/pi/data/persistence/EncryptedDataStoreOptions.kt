package com.pi.data.persistence

import com.google.crypto.tink.Aead

/** A class holding DataStore encryption options. */
class EncryptedDataStoreOptions internal constructor() {

    /** The associated data. By default, will be used data store file name. */
    var associatedData: ByteArray? = null

    /** The fallback [Aead]. You should provide it only if te datastore was previously encrypted with [Aead]. */
    var fallbackAead: Aead? = null
}
