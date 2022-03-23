package com.pi.data.remote

interface ProjectService {
    object Endpoint {
        const val mainPath = "/Market"

        object Market {
            const val productsPath = "products"
        }
    }
}
