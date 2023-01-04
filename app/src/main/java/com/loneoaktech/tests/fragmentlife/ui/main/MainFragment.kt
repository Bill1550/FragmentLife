package com.loneoaktech.tests.fragmentlife.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.loneoaktech.tests.fragmentlife.databinding.FragmentMainBinding
import com.loneoaktech.tests.fragmentlife.support.LifecycleBindingHolder
import com.loneoaktech.tests.fragmentlife.support.lazyViewBinding
import com.loneoaktech.tests.fragmentlife.support.withViews
import java.time.LocalTime

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private val bindingHolder = lazyViewBinding { FragmentMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainFragment", "onCreate")
        retainInstance = true
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

//        Log.i("MainFragment", "this should crash: ${bindingHolder.root}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("MainFragment", "onCreateView")

        return bindingHolder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingHolder.withViews {
            message.text = LocalTime.now().toString()
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