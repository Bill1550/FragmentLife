package com.loneoaktech.tests.fragmentlife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import androidx.lifecycle.lifecycleScope
import com.loneoaktech.tests.fragmentlife.ui.main.MainFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        stutterFragment()
    }

    /**
     * Detach and then reattach the fragment after a delay.
     * Testing the view lifecycle.
     */
    private fun stutterFragment() {

        lifecycleScope.launch {

            delay(2000)

            supportFragmentManager.findFragmentById(R.id.container)?.let { frag ->
                Log.i("MainActivity", "Detaching main fragment")
                supportFragmentManager.commit {
                    detach(frag)
                }

                delay(2000)

                Log.i("MainActivity", "Reattaching main fragment")
                supportFragmentManager.commit {
                    attach(frag)
                }
            }
        }
    }
}