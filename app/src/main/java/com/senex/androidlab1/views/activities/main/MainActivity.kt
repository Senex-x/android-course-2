package com.senex.androidlab1.views.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.repositories.NoteRepository
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppDatabaseMain.init(applicationContext)

        NoteRepository.add(Note(
            null,
            "Header",
            "Content",
            Date()
        ))
    }
}

