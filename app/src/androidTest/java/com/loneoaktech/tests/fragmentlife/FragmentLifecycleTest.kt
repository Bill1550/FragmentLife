package com.loneoaktech.tests.fragmentlife

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.loneoaktech.tests.fragmentlife.ui.main.MainFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentLifecycleTest {

    @Test
    fun testMainFragment() {

        println("--- running test")

        val scenario = launchFragmentInContainer<MainFragment>()

        println("--- fragment created")
        scenario.onFragment { fragment->
            println(">>> Current State=${fragment.lifecycle.currentState.name}")
            println("--- has views: ${fragment.view != null}")
        }
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }
}