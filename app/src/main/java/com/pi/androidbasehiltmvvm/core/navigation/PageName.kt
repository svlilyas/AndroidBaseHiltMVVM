package com.pi.androidbasehiltmvvm.core.navigation

/**
 * A class for managing app's pages or
 * to use DeepLink feature and navigate to exact page easily
 */
object PageName {
    /**
     * Note app's main domain
     */
    private const val myAppMain: String = "https://myApp.com.tr"

    /**
     * Base pages
     */
    object PreLogin {
        private const val preLogin: String = "$myAppMain/preLogin"

        /**
         * Page for listing all notes
         */
        const val NOTE_LIST_PAGE: String = "$preLogin/noteList"

        /**
         * Page for note create and edit
         */
        const val NOTE_CREATE_EDIT_PAGE: String = "$preLogin/noteCreateEdit"
    }
}