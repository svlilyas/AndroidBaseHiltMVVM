package com.pi.androidbasehiltmvvm.core.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pi.androidbasehiltmvvm.core.binding.ViewBinding.gone
import com.pi.androidbasehiltmvvm.core.binding.ViewBinding.visible
import com.pi.androidbasehiltmvvm.core.common.DateHelper
import com.pi.androidbasehiltmvvm.core.utils.AppConstants.Companion.DATE_DEFAULT

object TextViewBinding {

    @JvmStatic
    @BindingAdapter(
        value = ["app:modifiedTimeStamp"],
        requireAll = false
    )
    fun TextView.setLastModifiedDate(
        modifiedAt: Long?
    ) {
        this.text =
            DateHelper.formatDateFromTimeStamp(
                timeStamp = modifiedAt ?: 0,
                dateFormat = DATE_DEFAULT
            )
    }

    @JvmStatic
    @BindingAdapter(
        value = ["app:createdAt", "app:modifiedAt"],
        requireAll = false
    )
    fun TextView.setVisibleIfModified(
        createdAt: Long?,
        modifiedAt: Long?
    ) {
        if ((modifiedAt ?: 0) > (createdAt ?: 0)) {
            this.visible = true
        } else {
            this.gone = true
        }
    }
}
