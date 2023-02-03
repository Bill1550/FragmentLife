package com.loneoaktech.tests.fragmentlife.support

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KProperty

/**
 * Delegate to simplify the creation of a LifecycleBindingHolder.
 * Ensures that viewLifecycleOwner isn't referenced prematurely.
 */
class LazyViewBindingDelegate<T : ViewBinding>(
    private val creator: (ViewGroup?)->T
) {
    companion object {
        @VisibleForTesting
        val instances = AtomicInteger()
    }

    init {
        instances.incrementAndGet()
    }

    private var holder: LifecycleBindingHolder<T>? = null

    operator fun getValue(thisRef: Fragment, property: KProperty<*> ): LifecycleBindingHolder<T> {
        if (holder == null) {
            holder = LifecycleBindingHolder(thisRef.viewLifecycleOwner, creator)
        }
        return holder!!
    }

    @VisibleForTesting
    val isBound: Boolean
        get() = holder != null

}




