package com.senex.androidlab1.views.fragments.edit

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.LocationServices
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.FragmentAddEditBinding
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.utils.formatDate
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.utils.toast
import com.senex.androidlab1.views.activities.main.MainViewModel
import java.util.*

class AddEditFragment : Fragment() {
    private var _isEditing: Boolean? = null
    private val isEditing: Boolean
        get() = _isEditing!!

    private var _binding: FragmentAddEditBinding? = null
    private val binding
        get() = _binding!!

    private val args: AddEditFragmentArgs by navArgs()
    private var oldNote: Note? = null

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)
        _isEditing = args.noteId != -1L

        if (isEditing) {
            oldNote = mainViewModel.get(args.noteId)
        }

        binding.run {
            initTextFields()
            initSaveLocationCheckbox()
            initSetDateButton()
            initSaveButton()
        }

        return binding.root
    }

    private var targetDateCalendar: Calendar? = null

    private fun FragmentAddEditBinding.initSetDateButton() {
        setTargetDateButton.setOnClickListener {
            DatePickerFragment { _, year, month, day ->
                targetDate.text = requireContext().getString(
                    R.string.text_target_date_is, day, month, year
                )
                targetDateCalendar = Calendar.getInstance().apply {
                    set(year, month, day, 0, 0, 0)
                }
            }.show(
                parentFragmentManager,
                DatePickerFragment::class.java.simpleName
            )
        }
    }

    private fun FragmentAddEditBinding.initSaveLocationCheckbox() {
        saveLocationCheck.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isLocationAccessGranted) {
                requestLocationAccessPermission()
            }
        }
    }

    private val isLocationAccessGranted: Boolean
        get() = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationAccessPermission() {
        locationPermissionRequest.launch(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            binding.saveLocationCheck.isChecked = false
        }
    }

    private fun FragmentAddEditBinding.initSaveButton() {
        saveButton.setOnClickListener {
            val targetDate: Calendar? = targetDateCalendar

            if (headerEditText.text.toString().isBlank()) {
                requireContext().toast(
                    getString(R.string.error_note_must_have_header)
                )
                return@setOnClickListener
            }

            var longitude: Double? = null
            var latitude: Double? = null
            if (!isEditing && saveLocationCheck.isChecked) {
                getLocation { location ->
                    location?.let {
                        longitude = it.longitude
                        latitude = it.latitude
                    }

                    saveNote(
                        targetDate,
                        longitude, latitude
                    )

                    navigateToListFragment()
                }
            } else {
                saveNote(
                    targetDate,
                    longitude, latitude
                )

                navigateToListFragment()
            }
        }
    }

    private fun Fragment.navigateToListFragment() {
        findNavController().navigate(
            AddEditFragmentDirections
                .actionEditFragmentToListFragment()
        )
    }

    private fun FragmentAddEditBinding.saveNote(
        targetDate: Calendar?,
        longitude: Double?, latitude: Double?,
    ) {
        val note = Note(
            if (isEditing && oldNote != null) oldNote!!.id else null,
            headerEditText.text.toString(),
            contentEditText.text.toString(),
            Calendar.getInstance(), targetDate,
            longitude, latitude,
        )

        if (isEditing) {
            mainViewModel.update(note)
        } else {
            mainViewModel.add(note)
        }
    }

    private fun getLocation(callback: (Location?) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(callback)
        } catch (exception: SecurityException) {
            exception.printStackTrace()
            callback(null)
        }
    }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun FragmentAddEditBinding.initTextFields() {
        val oldNote = this@AddEditFragment.oldNote

        if (isEditing && oldNote != null) {
            headerEditText.setText(oldNote.header)
            contentEditText.setText(oldNote.content)
            saveLocationCheck.visibility = View.GONE
            oldNote.targetDate?.let { targetDate.text = formatDate(it.time) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _isEditing = null
    }
}
