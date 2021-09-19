package com.senex.androidlab1.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.senex.androidlab1.R
import com.senex.androidlab1.examples.ExampleChildBarImpl
import com.senex.androidlab1.examples.ExampleChildFooImpl
import com.senex.androidlab1.utils.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val e1 = ExampleChildFooImpl("data", "optional")
        val e2 = ExampleChildBarImpl("data")

        e1.foo("string").toString().log()
        e2.bar("string").log()
    }
}

