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
class LifecycleBindingHolder<T : ViewBinding>(
    lifecycleOwner: LifecycleOwner,
    private val creator: ()->T
) {
    private var _binding: T? = null
    private var useCount: Int = 0

    init {
        lifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

                    Log.i("LifecycleBindingHolder", "lifecycle event=${event.name}")
                    when (event) {
                        Lifecycle.Event.ON_DESTROY -> clear()
                        else -> {
                        } // don't care
                    }
                }
            }
        )
    }


    val binding: T
        get() {
            return _binding ?: let {
                useCount++
                if ( useCount > 1)
                    Log.w("LifecycleBindingHolder","Binding holder reused! count=$useCount") // not a real error, just very interesting!
                creator().also { _binding = it }
            }
        }

    val root: View
        get() = binding.root

    fun clear() {
        Log.i("LifecycleBindingHolder", "clearing binding holder")
        _binding = null
    }
}

fun <T: ViewBinding> LifecycleOwner.lazyViewBinding(creator: ()->T): LifecycleBindingHolder<T>
= LifecycleBindingHolder(this, creator)

fun <T: ViewBinding, U: Any?> LifecycleBindingHolder<T>.withViews(block: T.()->U): U {
    return this.binding.block()
}

//fun <T: ViewBinding> Fragment.lazyViewBinding2(creator: ()->T): LifecycleBindingHolder<T>
// by lazy { LifecycleBindingHolder(this.viewLifecycleOwner, creator) }