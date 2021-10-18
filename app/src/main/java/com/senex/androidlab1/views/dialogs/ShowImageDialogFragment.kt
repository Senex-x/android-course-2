package com.senex.androidlab1.views.dialogs

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.DialogShowImageBinding

class ShowImageDialogFragment(private val sourceBitmap: Bitmap) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let {
            val binding = DialogShowImageBinding.inflate(layoutInflater)
            binding.dialogImageView.setImageBitmap(sourceBitmap)

            AlertDialog.Builder(it).run {
                setView(binding.root)
                setTitle(getString(R.string.title_received_image))
                setPositiveButton(getString(R.string.title_ok)) { _, _ -> dismiss() }
                create()
            }
        } ?: throw IllegalStateException("Activity cannot be null")
}