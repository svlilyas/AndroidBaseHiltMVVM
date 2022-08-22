package com.pi.androidbasehiltmvvm.core.platform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pi.androidbasehiltmvvm.R

abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel>(
    @LayoutRes private val layoutId: Int,
    private val viewModelClass: Class<VM>
) : Fragment() {

    open var useSharedViewModel: Boolean = false

    lateinit var binding: DB
    lateinit var viewModelProvider: ViewModelProvider

    val viewModel by lazy {
        if (useSharedViewModel) {
            ViewModelProvider(requireActivity())[viewModelClass]
        } else {
            ViewModelProvider(this)[viewModelClass]
        }
    }

    lateinit var pageTitle: String

    var isBackActive: Boolean = false

    protected val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSoftInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setUpViews()
        observeData()
        getScreenKey()
    }

    open fun setUpViews() {}

    open fun observeData() {}

    abstract fun getScreenKey(): String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    fun openBrowser(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(
            Intent.createChooser(
                intent,
                "Select a browser"
            )
        )
    }

    internal fun hideSoftInput() {
        activity?.let {
            (it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                it.currentFocus
                hideSoftInputFromWindow(
                    (it.currentFocus ?: View(requireContext())).windowToken,
                    0
                )
            }
        }
    }

    internal fun showProgressView() {
    }

    internal fun hideProgressView() {
    }

    open fun finishApp() {
        requireActivity().finish()
        val finishIntent =
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        startActivity(finishIntent)
    }

    fun encodeBase64(string: String): String {
        return Base64.encodeToString(
            string.toByteArray(),
            NO_WRAP
        )
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return when (transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_FADE -> if (enter) {
                AnimationUtils.loadAnimation(activity, R.anim.fade_in_left)
            } else {
                AnimationUtils.loadAnimation(activity, R.anim.fade_out_left)
            }
            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> if (enter) {
                AnimationUtils.loadAnimation(activity, R.anim.fade_in_left)
            } else {
                AnimationUtils.loadAnimation(activity, R.anim.fade_out_left)
            }
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> if (enter) {
                AnimationUtils.loadAnimation(activity, R.anim.fade_in_left)
            } else {
                AnimationUtils.loadAnimation(activity, R.anim.fade_out_left)
            }
            else -> if (enter) {
                AnimationUtils.loadAnimation(activity, R.anim.fade_in_left)
            } else {
                AnimationUtils.loadAnimation(activity, R.anim.fade_out_left)
            }
        }
    }
}
