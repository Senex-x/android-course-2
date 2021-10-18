package com.senex.androidlab1.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.fragments.PageOneFragment
import com.senex.androidlab1.views.fragments.PageThreeFragment
import com.senex.androidlab1.views.fragments.PageTwoFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.init()
    }

    private fun ActivityMainBinding.init() {
        supportFragmentManager.commit {
            add(R.id.view_fragment_container, PageOneFragment(), "PageOneFragment")
            setReorderingAllowed(true)
        }

        buttonPageOne.setOnClickListener {
            replaceFragmentWith<PageTwoFragment>("PageOneFragment")
        }

        buttonPageTwo.setOnClickListener {
            replaceFragmentWith<PageTwoFragment>("PageTwoFragment")
        }

        buttonPageThree.setOnClickListener {
            replaceFragmentWith<PageThreeFragment>("PageThreeFragment")
        }
    }

    private inline fun <reified T : Fragment> replaceFragmentWith(
        fragmentName: String
    ) {
        supportFragmentManager.commit {
            replace<T>(R.id.view_fragment_container, fragmentName)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}

