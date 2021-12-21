package com.senex.androidlab1.views.activities.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.senex.androidlab1.R
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.utils.log
import java.util.*


// TODO: add binding to dev
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        AppDatabaseMain.init(applicationContext)
/*
        NoteRepository.add(Note(
            null,
            "Header",
            "Content",
            Calendar.getInstance(),
            null,
            null,
            null,
        ))
*/
        log("Database snapshot: " +
                AppDatabaseMain.database.noteDao().getAll().toString()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete_all -> {
            mainViewModel.removeAll()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}

