package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.senex.androidlab1.R
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.databinding.FragmentInfoBinding
import com.senex.androidlab1.models.User
import java.text.SimpleDateFormat
import java.util.*

class InfoFragment : Fragment() {
    private val args: InfoFragmentArgs by navArgs()
    private var _binding: FragmentInfoBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        val user = AppDatabaseMain.database
            .userDao()
            .get(args.userId)

        binding.run {
            fragmentInfoToolbar.init()
            fragmentInfoCollapsingToolbarLayout.init(user)
            fragmentInfoToolbarExpandedImage.init(user)
            initFields(user)
        }

        return binding.root
    }

    private fun Toolbar.init(): Unit =
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(this@init)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun FragmentInfoBinding.initFields(user: User) {
    status.text = user.status
    email.text = user.email
    val dateString = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        .format(user.birthDate)
    birthDate.text = dateString.substring(0, dateString.lastIndexOf('.') + 5)
    description.text = user.description
}

private fun CollapsingToolbarLayout.init(user: User) {
    setExpandedTitleTextAppearance(R.style.text_view_style_toolbar_expanded)
    setExpandedTitleColor(resources.getColor(R.color.white))

    title = user.nickname
}

private fun ImageView.init(user: User) {
    setImageResource(
        user.imageId ?: R.drawable.image_profile_default
    )
}
