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
import android.widget.Toast

import android.content.ContentResolver
import java.lang.Exception


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
        binding.mainImageViewPicture.setImageBitmap(grabImage(imageUri))

        val bitmap = data?.extras?.get("data") as? Bitmap
        bitmap?.let {
            //toast("${it.height} ${it.width}")
            //binding.mainImageViewPicture.setImageBitmap(it)
        } ?: toast(R.string.error_unexpected_error)
    }

    lateinit var imageUri : Uri

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

                           imageUri = Uri.fromFile(File(externalCacheDir.toString() + "/image.png"))
                        launcherTakePhoto.launch(
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            }
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

    fun grabImage(uri: Uri): Bitmap {
        this.contentResolver.notifyChange(uri, null)
        val cr = this.contentResolver
        val bitmap: Bitmap

        bitmap = MediaStore.Images.Media.getBitmap(cr, uri)
        return bitmap;

    }

    fun internal() {

        /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        try {
            // place where to store camera taken picture
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        } catch (Exception e) {
            Log.v(TAG, "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", 10000);
            return false;
        }
        mImageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        //start camera intent
        activity.startActivityForResult(this, intent, MenuShootImage);

        private File createTemporaryFile(String part, String ext) throws Exception
        {
            File tempDir = Environment . getExternalStorageDirectory ();
            tempDir = new File (tempDir.getAbsolutePath() + "/.temp/");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            return File.createTempFile(part, ext, tempDir);
        }

         */
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