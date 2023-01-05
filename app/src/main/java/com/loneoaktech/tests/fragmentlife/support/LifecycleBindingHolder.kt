package com.loneoaktech.tests.fragmentlife.support

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * Small class to create a lazy holder for a view binding
 */
class LifecycleBindingHolder<T : ViewBinding>(
    private val lifecycleOwner: LifecycleOwner,
    private val creator: (ViewGroup?)->T
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

    fun bind(container: ViewGroup?): View {
        return createBinding(container).also {
            _binding = it
        }.root
    }


    val binding: T
        get() {
            return checkNotNull(_binding){"FragmentBindingHolder.bind has not been called"}
        }

    val root: View
        get() = binding.root

    private fun createBinding(container: ViewGroup?): T {
        useCount++
        if (useCount > 1)
            Log.i("FragmentBindingHolder", "Reusing binding holder")

        lifecycleOwner.lifecycle.addObserver(observer)
        return creator(container).apply {
            Log.i("FragmentBindingHolder","Created binding for ${this.javaClass.simpleName}")
        }
    }


    private fun clear(owner: LifecycleOwner) {
        Log.i("FragmentBindingHolder", "clearing binding holder")
        _binding = null
        owner.lifecycle.removeObserver(observer)
    }
}

//fun <T: ViewBinding> Fragment.lazyViewBinding(creator: (ViewGroup?)->T): LifecycleBindingHolder<T> =
//    LifecycleBindingHolder(this, creator)

fun <T: ViewBinding, U: Any?> LifecycleBindingHolder<T>.withViews(block: T.()->U): U {
    return this.binding.block()
}




