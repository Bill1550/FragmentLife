package com.loneoaktech.tests.fragmentlife

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.loneoaktech.tests.fragmentlife.support.LazyViewBindingDelegate
import com.loneoaktech.tests.fragmentlife.support.LifecycleBindingHolder
import com.loneoaktech.tests.fragmentlife.ui.main.MainFragment
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FragmentLifecycleTest {

    /**
     * Just a test of the test fixture.
     * Make sure fragment can be instantiated and run through its lifecycle.
     * (Test was created due to dependency issues w/ androidx fragment test library)
     */
    @Test
    fun verifyBasicFragmentTest() {

        val numDelegates = LazyViewBindingDelegate.instances.get()
        val numBindings = LazyViewBindingDelegate.instances.get()
        println("Num delegates=$numDelegates, num bindings=$numBindings")

        val fragment = TestFragment.newInstance()
        val scenario = launchFragmentInContainer(
            initialState = Lifecycle.State.INITIALIZED
        ) { fragment }

        assertEquals( numDelegates+1, LazyViewBindingDelegate.instances.get() )
        assertEquals( numBindings, LifecycleBindingHolder.instances.get() )

        println("--- fragment created")
        scenario.withFragment {
            println(">>> Current State=${lifecycle.currentState.name}")
            assertEquals( Lifecycle.State.INITIALIZED, lifecycle.currentState)
            assertNull( fragment.view )
        }

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.withFragment {
            println(">>> Current State=${lifecycle.currentState.name}")
            assertEquals( Lifecycle.State.RESUMED, lifecycle.currentState)
            assertNotNull( view )
            assertTrue("Binding holder is reporting not bound", bindingHolder.isBound)
        }

        assertEquals( numBindings+1, LifecycleBindingHolder.instances.get() )

        scenario.moveToState(Lifecycle.State.DESTROYED)
//        scenario.withFragment {
//  ---> Doesn't work, because fragment has been removed from fragment mgr
//           assertEquals( Lifecycle.State.DESTROYED, lifecycle.currentState )
//        }
        assertNull( fragment.view )
        assertFalse( fragment.bindingHolder.isBound )

        println("After test, num delegates=${LazyViewBindingDelegate.instances.get()}, num bindings=${LifecycleBindingHolder.instances.get()}")
    }

    @Test(expected = java.lang.IllegalStateException::class)
    fun testBindingNotInitialization() {
        val fragment = TestFragment.newInstance()
        val scenario = launchFragmentInContainer(
            initialState = Lifecycle.State.INITIALIZED
        ) { fragment }

        // Try to test the delegate without calling get
//        fragment::bindingHolder.isAccessible = true  // --- not working, bug described in Kotlin's db: https://youtrack.jetbrains.com/issue/KT-27585
//                                                     // is not fixed in Kotlin 1.8.0
//        assertNotNull( fragment::bindingHolder.getDelegate() )
//        val delegate = fragment::bindingHolder.getDelegate()
//        println("binding delegate is bound = ${(delegate as? LazyViewBindingDelegate<*>)?.isBound}")

        scenario.withFragment {
            try {
                // Accessing delegate should cause binding holder to be instantiated before its time.
                val isBound = fragment.bindingHolder.isBound
                assert(false) { "IllegalStateException should have been thrown, bound=$isBound" }
            } catch( ex: java.lang.IllegalStateException) {
                println( "expected exception thrown, msg=${ex.message}")
                throw ex
            }

        }
    }

    @Test
    fun testBindingInitialized() {
        val fragment = TestFragment.newInstance()
        val scenario = launchFragmentInContainer(
            initialState = Lifecycle.State.INITIALIZED
        ) { fragment }
//
//        val bindingHolder by LazyViewBindingDelegate { container ->
//            FragmentMainBinding.inflate(fragment.layoutInflater, container, false)
//        }

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.withFragment {
            println(">>> Current State=${lifecycle.currentState.name}")
            assertEquals( Lifecycle.State.RESUMED, lifecycle.currentState)
            assertNotNull( view )
            assertTrue("Binding holder is reporting not bound", bindingHolder.isBound)
        }
    }

    @Test
    fun testFragmentRecreateAfterResume() {

        val fragment = TestFragment.newInstance()
        val scenario = launchFragmentInContainer(
            initialState = Lifecycle.State.INITIALIZED
        ) { fragment }

        scenario.moveToState(Lifecycle.State.RESUMED )
        scenario.withFragment {
            assertTrue(bindingHolder.isBound)
        }

        scenario.recreate()
        scenario.withFragment {
            assertEquals( Lifecycle.State.RESUMED, lifecycle.currentState )
            assertTrue(bindingHolder.isBound)
            assertEquals(2, bindingHolder.bindCount)
        }

    }

    @Test(expected = java.lang.IllegalStateException::class )
    fun testFragmentRecreateAfterCreate() {

        val fragment = TestFragment.newInstance()
        val scenario = launchFragmentInContainer(
            initialState = Lifecycle.State.INITIALIZED
        ) { fragment }

        scenario.moveToState(Lifecycle.State.CREATED )

        scenario.recreate()
        scenario.withFragment {
            assertEquals( Lifecycle.State.CREATED, lifecycle.currentState )
            try {
                val isBound = bindingHolder.isBound
                assert(false){ "IllegalStateException should have been thrown, bound=$isBound" }
            }catch ( ex: java.lang.IllegalStateException){
                println( "expected exception thrown, msg=${ex.message}")
                throw ex
            }
        }
    }


}

