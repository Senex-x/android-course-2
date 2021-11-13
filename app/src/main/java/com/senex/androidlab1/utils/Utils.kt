package com.senex.androidlab1.utils

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.R
import com.senex.androidlab1.models.User
import io.github.serpro69.kfaker.faker
import java.util.*
import kotlin.collections.ArrayList

internal fun Context.toast(message: String?) =
    message?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    } ?: Unit

internal fun Context.toast(messageId: Int?) =
    messageId?.let {
        toast(this.getString(it))
    } ?: Unit

internal fun Int?.toast(context: Context) =
    context.toast(this)

internal fun String?.toast(context: Context) =
    context.toast(this)

internal fun log(message: String?) =
    Log.d("app-debug", message ?: "null")

internal fun String.present() =
    isNotEmpty() && isNotBlank()

internal fun generateImageResources(amount: Int): List<Int> {
    val list = ArrayList<Int>()
    val random = Random()

    val imageIdList = listOf(
        R.drawable.image_profile_1,
        R.drawable.image_profile_2,
        R.drawable.image_profile_3,
        R.drawable.image_profile_4,
    )

    for(i in 0 until amount) {
        list.add(
            imageIdList[random.nextInt(imageIdList.size)]
        )
    }

    return list
}

internal fun generateUsers(amount: Int): MutableList<User> {
    val list = ArrayList<User>()
    val random = Random()

    val faker = faker {
        fakerConfig {
            this.random = random
            locale = "en"
        }
    }

    for (i in 0 until amount) {
        list.add(
            User(
                null,
                faker.funnyName.name(),
                faker.quote.famousLastWords()
            )
        )
    }

    return list
}

class MarginItemDecoration(
    private val margin: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // Adapter should always present
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = margin
        }
        outRect.bottom = margin
    }
}