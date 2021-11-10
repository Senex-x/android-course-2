package com.senex.androidlab1.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.User
import com.senex.androidlab1.utils.generateUsers
import com.senex.androidlab1.utils.log
import io.github.serpro69.kfaker.faker
import java.util.*
import kotlin.collections.ArrayList

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

