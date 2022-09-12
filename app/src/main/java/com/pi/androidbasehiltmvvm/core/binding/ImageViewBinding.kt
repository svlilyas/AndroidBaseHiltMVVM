package com.pi.androidbasehiltmvvm.core.binding

import android.graphics.drawable.Drawable
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import com.pi.androidbasehiltmvvm.core.binding.ViewBinding.gone
import com.pi.androidbasehiltmvvm.core.binding.ViewBinding.visible

object ImageViewBinding {

    @JvmStatic
    @BindingAdapter(
        value = ["app:imageUrl", "app:placeholder", "app:errorDrawable"],
        requireAll = false
    )
    fun ImageView.loadImage(
        url: String?,
        placeholder: Drawable?,
        errorDrawable: Drawable?
    ) {
        if (URLUtil.isValidUrl(url)) {
            this.visible = true
            this.load(url) {
                placeholder(placeholder)
                error(errorDrawable)
                networkCachePolicy(CachePolicy.ENABLED)
                memoryCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED)
                crossfade(true)
            }
        } else {
            this.gone = true
        }
    }
}
