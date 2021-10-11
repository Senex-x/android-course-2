package com.senex.androidlab1.views.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.utils.toast


/*
            1) отправляет неявный интент в систему(на ваш выбор)
            и обрабатывает результат с помощью onActivityResult.
            Ответ можете вывести на экран используя Snackbar.
         */


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var launcherPickPhoto: ActivityResultLauncher<Intent>
    private lateinit var launcherTakePhoto: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launcherPickPhoto = createLauncher {
            pickImageAction()
        }

        launcherTakePhoto = createLauncher {
            takeImageAction()
        }

        binding.mainButtonUpload.setOnClickListener {
            selectImage()
        }
    }

    private fun createLauncher(func : ActivityResult.() -> Unit) =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                func(result)
            } else {
                toast(R.string.error_image_not_picked)
            }
        }

    private fun  ActivityResult.pickImageAction() {
        binding.mainImageViewPicture.setImageFromUri(
            data?.data
        ) ?: toast(R.string.error_unexpected_error)
    }

    private fun ActivityResult.takeImageAction() {
        val bitmap = data?.extras?.get("data") as? Bitmap
        bitmap?.let {
            binding.mainImageViewPicture.setImageBitmap(it)
        } ?: toast(R.string.error_unexpected_error)
    }

    private fun selectImage() {
        val options = arrayOf(
            getString(R.string.title_take_photo),
            getString(R.string.title_choose_from_gallery),
            getString(R.string.title_cancel)
        )
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_choose_photo))
            setItems(options) { dialog, item ->
                when (options[item]) {
                    getString(R.string.title_take_photo) -> {
                        launcherTakePhoto.launch(
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        )
                    }
                    getString(R.string.title_choose_from_gallery) -> {
                        launcherPickPhoto.launch(
                            Intent(Intent.ACTION_PICK).apply {
                                type = "image/*"
                            }
                        )
                    }
                    getString(R.string.title_cancel) -> {
                        dialog.dismiss()
                    }
                }
            }
            show()
        }
    }
}

private fun ImageView.setImageFromUri(uri: Uri?) =
    context.obtainBitmapOrNull(uri)?.let { bitmap ->
        setImageBitmap(bitmap)
    }

private fun Context.obtainBitmapOrNull(uri: Uri?) =
    uri?.let {
        BitmapFactory.decodeStream(
            contentResolver.openInputStream(it)
        )
    }