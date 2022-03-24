package com.pi.androidbasehiltmvvm.core.common.fakedata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notification(
    var id: Int = 0,
    var title: String = "",
    var subTitle: String = "",
    var navigationUrl: String = "/route/NewProduct",
    var desc: String = "Description Text",
    var read: Boolean = false
) : Parcelable

object Notifications {
    fun fakeData() = arrayListOf(
        Notification(
            id = 0,
            title = "Notification Title 1",
            subTitle = "Notification SubTitle 1",
            read = false
        ),
        Notification(
            id = 1,
            title = "Notification Title 1",
            subTitle = "Notification SubTitle 1",
            read = false
        ),
        Notification(
            id = 2,
            title = "Notification Title 1",
            subTitle = "Notification SubTitle 1",
            read = true
        ),
        Notification(
            id = 3,
            title = "Notification Title 1",
            subTitle = "Notification SubTitle 1",
            read = true
        ),
        Notification(
            id = 4,
            title = "Notification Title 1",
            subTitle = "Notification SubTitle 1",
            read = true
        )
    )
}
