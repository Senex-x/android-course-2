package com.senex.androidlab1.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.senex.androidlab1.databinding.FragmentDialogAddItemBinding
import com.senex.androidlab1.utils.toast

class AddItemDialog(
    val onPositiveClick: (String, String, String) -> Unit
) : DialogFragment() {
    private lateinit var binding: FragmentDialogAddItemBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        activity?.let {
            binding = FragmentDialogAddItemBinding.inflate(layoutInflater)
            binding.run {
                    AlertDialog.Builder(it)
                        .setView(root)
                        .setPositiveButton("R.string.ok", onPositiveButtonClick)
                        .setNegativeButton("R.string.cancel") { dialog, _ ->
                            dialog.cancel()
                        }
                        .create()
                }
        } ?: throw IllegalStateException("Activity is null")

    private val onPositiveButtonClick = { _: DialogInterface, _: Int ->
        binding.run {
            val name = textInputName.text.toString()
            val description = textInputDescription.text.toString()
            val position = textInputPosition.text.toString()

            if (name.isNotEmpty() &&
                description.isNotEmpty()
            ) {
                onPositiveClick(name, description, position)
            } else {
                requireContext().toast("Please, fill in all fields")
            }
        }
    }
}