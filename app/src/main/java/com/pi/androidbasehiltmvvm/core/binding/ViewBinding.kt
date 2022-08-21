package com.pi.androidbasehiltmvvm.core.binding

import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.databinding.BindingAdapter

object ViewBinding {

    @JvmStatic
    @BindingAdapter("toast")
    fun View.bindToast(text: String?) {
        if (!text.isNullOrEmpty()) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    @set:BindingAdapter("gone")
    var View.gone: Boolean
        get() = visibility == View.GONE
        set(value) {
            visibility = if (value) View.GONE else View.VISIBLE
        }

    @JvmStatic
    @set:BindingAdapter("visible")
    var View.visible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value) View.VISIBLE else View.GONE
        }

    @JvmStatic
    @set:BindingAdapter("invisible")
    var View.invisible: Boolean
        get() = visibility == View.INVISIBLE
        set(value) {
            visibility = if (value) View.INVISIBLE else View.VISIBLE
        }

    @JvmStatic
    @BindingAdapter("onBackPressed")
    fun View.onBackPressed(onBackPress: Boolean) {
        val context = this.context
        if (onBackPress && context is OnBackPressedDispatcherOwner) {
            setOnClickListener {
                context.onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}
