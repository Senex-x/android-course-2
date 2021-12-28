package com.senex.androidlab1.ui.fragments.edit

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
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.utils.toast
import com.senex.androidlab1.viewmodels.MainViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.abs

class AddEditFragment : Fragment() {
    private var _binding: FragmentAddEditBinding? = null
    private val binding
        get() = _binding!!

    private var _isEditing: Boolean? = null
    private val isEditing: Boolean
        get() = _isEditing!!

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private val args: AddEditFragmentArgs by navArgs()
    private var oldNote: Note? = null
    private var targetDateCalendar: Calendar? = null
    private var deferredLocation: Deferred<Location?>? = null
    private var locationLastRecordTime: Long? = null

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

    private fun FragmentAddEditBinding.initTextFields() {
        oldNote?.let {
            headerEditText.setText(it.header)
            contentEditText.setText(it.content)
            saveLocationCheck.text =
                getString(R.string.message_update_saved_location)

            it.targetDate?.let { date ->
                val calendar = Calendar.getInstance().apply { time = date }
                targetDate.text = requireContext().getString(
                    R.string.text_target_date_is,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
                )
            }
        }
    }


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
            if (isChecked) {
                if (!isLocationAccessGranted) {
                    log("Requesting location access permission")

                    requestLocationAccessPermission()
                } else {
                    setDeferredLocation()
                }
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
        } else {
            setDeferredLocation()
        }
    }

    private fun setDeferredLocation() {
        if (deferredLocation == null ||
            locationLastRecordTime differByMoreThanSecondFrom System.currentTimeMillis()
        ) {
            log("Saving location")
            requireContext().toast("Saving location")

            locationLastRecordTime = System.currentTimeMillis()
            deferredLocation = getLocationAsync()
        }
    }

    private fun FragmentAddEditBinding.initSaveButton() {
        saveButton.setOnClickListener {
            val targetDate: Date? = targetDateCalendar?.time

            if (headerEditText.text.toString().isBlank()) {
                requireContext().toast(
                    getString(R.string.error_note_must_have_header)
                )
                return@setOnClickListener
            }

            var longitude: Double? = oldNote?.longitude
            var latitude: Double? = oldNote?.latitude

            if (saveLocationCheck.isChecked) {
                deferredLocation?.let { deferred: Deferred<Location?> ->
                    runBlocking {
                        deferred.await()?.let { location ->
                            longitude = location.longitude
                            latitude = location.latitude
                        }
                    }
                }
            }

            saveNote(
                targetDate,
                longitude, latitude
            )

            navigateToListFragment()
        }
    }

    private infix fun Long?.differByMoreThanSecondFrom(time: Long) =
        this?.let {
            abs(it - time) > 1000
        } ?: true

    private fun Fragment.navigateToListFragment() {
        findNavController().navigate(
            AddEditFragmentDirections
                .actionEditFragmentToListFragment()
        )
    }

    private fun FragmentAddEditBinding.saveNote(
        targetDate: Date?,
        longitude: Double?, latitude: Double?,
    ) {
        val note = Note(
            if (isEditing && oldNote != null) oldNote!!.id else null,
            headerEditText.text.toString(),
            contentEditText.text.toString(),
            Date(), targetDate,
            longitude, latitude,
        )

        if (isEditing) {
            mainViewModel.update(note)
        } else {
            mainViewModel.add(note)
        }
    }

    private fun getLocationAsync() = MainScope().async {
        getLocation()
    }

    private suspend fun getLocation(): Location? {
        return suspendCoroutine { continuation ->
            try {
                fusedLocationClient
                    .lastLocation
                    .addOnSuccessListener {
                        continuation.resume(it)
                    }
            } catch (exception: SecurityException) {
                continuation.resume(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _isEditing = null
        deferredLocation?.cancel()
        deferredLocation = null
        // In my case there is no reason to stop all coroutines operating with database,
        // since they are affecting neither UI elements nor instance of AddEditFragment.
        // mainViewModel.onDestroy()
    }
}
