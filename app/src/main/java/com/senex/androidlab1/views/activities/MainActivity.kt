package com.senex.androidlab1.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.User
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

        userDao.insertAll(*generateUsers(5).toTypedArray())

        val users: List<User> = userDao.getAll()

        log(users.toString())
    }

    private fun generateUsers(amount: Int): MutableList<User> {
        val list = ArrayList<User>()
        val random = Random()

        val faker = faker {
            fakerConfig {
                this.random = random
                locale = "en"
            }
        }

        for (i in 1..amount) {
            list.add(
                User(
                    null,
                    faker.funnyName.name(),
                    random.nextBoolean(),
                    faker.quote.famousLastWords(),
                    faker.internet.email(),
                    Date(random.nextLong()),
                    faker.witcher.quotes()
                )
            )
        }

        return list
    }
}

