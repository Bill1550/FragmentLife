package com.loneoaktech.tests.fragmentlife.support

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KProperty

/**
 * Delegate to simplify the creation of a LifecycleBindingHolder
 */
class LazyViewBindingDelegate<T : ViewBinding>(
    private val creator: (ViewGroup?)->T
) {
    private var holder: LifecycleBindingHolder<T>? = null

    operator fun getValue(thisRef: Fragment, property: KProperty<*> ): LifecycleBindingHolder<T> {
        if (holder == null) {
            holder = LifecycleBindingHolder(thisRef.viewLifecycleOwner, creator)
        }
        return holder!!
    }
}