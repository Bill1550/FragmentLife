package com.loneoaktech.tests.fragmentlife.support

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * Small class to create a lazy holder for a view binding
 */
class FragmentBindingHolder<T : ViewBinding>(
    private val fragment: Fragment,
    private val creator: ()->T
) {
    private var _binding: T? = null
    private var useCount: Int = 0

    private val observer = LifecycleEventObserver { owner, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                clear(owner)
            }
            else -> {
            } // don't care
        }
    }

    val binding: T
        get() {
            return _binding ?: let {
                // need to create binding
                createBinding().also {
                    _binding = it
                }
            }
        }

    val root: View
        get() = binding.root

    private fun createBinding(): T {
        useCount++
        if (useCount > 1)
            Log.i("FragmentBindingHolder", "Reusing binding holder")

        fragment.viewLifecycleOwner.lifecycle.addObserver(observer)
        return creator().apply {
            Log.i("FragmentBindingHolder","Created binding for ${this.javaClass.simpleName}")
        }
    }


    private fun clear(owner: LifecycleOwner) {
        Log.i("FragmentBindingHolder", "clearing binding holder")
        _binding = null
        owner.lifecycle.removeObserver(observer)
    }
}

fun <T: ViewBinding> Fragment.lazyViewBinding(creator: ()->T): FragmentBindingHolder<T> =
    FragmentBindingHolder(this, creator)

fun <T: ViewBinding, U: Any?> FragmentBindingHolder<T>.withViews(block: T.()->U): U {
    return this.binding.block()
}
