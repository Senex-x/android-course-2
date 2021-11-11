package com.senex.androidlab1.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.database.AppDatabaseMain

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Singleton initialization
        AppDatabaseMain.init(this)

        val userDao = AppDatabaseMain.database.userDao()

        //userDao.deleteAll()

        userDao.insertAll(
            //*generateUsers(20).toTypedArray()
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

