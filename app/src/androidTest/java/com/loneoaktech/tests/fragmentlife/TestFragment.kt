package com.loneoaktech.tests.fragmentlife

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import com.loneoaktech.tests.fragmentlife.databinding.TestFragmentBinding
import com.loneoaktech.tests.fragmentlife.support.LazyViewBindingDelegate
import com.loneoaktech.tests.fragmentlife.support.withViews
import java.time.LocalTime

class TestFragment : Fragment() {

    companion object {
        fun newInstance() = TestFragment()
    }


    // create a binding holder using a delegate
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE )
    val bindingHolder by LazyViewBindingDelegate { container ->

        TestFragmentBinding.inflate(layoutInflater,container, false)
//        FragmentMainBinding.inflate(layoutInflater,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TestFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindingHolder.bind(container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This is the standard way to access views
        bindingHolder.withViews {
            message.text = LocalTime.now().toString() // show something that changes
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("MainFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainFragment", "OnDestroy")
    }


}