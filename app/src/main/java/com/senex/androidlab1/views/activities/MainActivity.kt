package com.senex.androidlab1.views.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.senex.androidlab1.BuildConfig
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.utils.toast
import com.senex.androidlab1.views.dialogs.ShowImageDialogFragment
import java.io.File
import java.io.FileOutputStream

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

        binding.mainButtonShare.setOnClickListener {
            val imageFile = File(externalCacheDir.toString() + "/image.png")

            // Puts image into imageFile to share it's URI
            FileOutputStream(imageFile).use {
                binding.mainImageViewPicture.getBitmap()
                    .compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            val uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + "." + localClassName + ".provider",
                imageFile
            )

            Intent().apply {
                action = Intent.ACTION_SEND
                setDataAndType(uri, "image/*")
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Shared from Android Lab App")
                startActivity(
                    Intent.createChooser(this, "Share image using")
                )
            }
        }

        receiveImageIntent()
    }

    private fun receiveImageIntent() {
        if (intent.action == Intent.ACTION_SEND && "image/*" == intent.type) {
            obtainBitmapOrNull(intent.data)?.let {
                ShowImageDialogFragment(it).show(
                    supportFragmentManager,
                    "ShowImageDialogFragment"
                )
            } ?: toast(R.string.error_unexpected_error)
        }
    }

    private fun createLauncher(func: ActivityResult.() -> Unit) =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                func(result)
            } else {
                toast(R.string.error_image_not_picked)
            }
        }

    private fun ActivityResult.pickImageAction() {
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

private fun ImageView.getBitmap() =
    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        draw(Canvas(this))
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