package com.sushant.mapanddatabasemvvm.ui.locationEntry

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.sushant.mapanddatabasemvvm.R
import com.sushant.mapanddatabasemvvm.database.model.LocationEntry
import com.sushant.mapanddatabasemvvm.database.model.UiLocationEntry
import com.sushant.mapanddatabasemvvm.databinding.FragmentItemListDialogListDialogBinding

class LocationAddMarkerFragment : BottomSheetDialogFragment() {

    private lateinit var locationAddMarkerViewModel: LocationAddMarkerViewModel
    private var _binding: FragmentItemListDialogListDialogBinding? = null
    private val binding get() = _binding
    private val latLong: LatLng? by lazy {
        arguments?.getParcelable(BUNDLE_KEY_LAT_LONG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        locationAddMarkerViewModel = ViewModelProvider(
            this, LocationAddMarkerViewModel
                .ViewModelFactory(requireActivity().application)
        )[LocationAddMarkerViewModel::class.java]
        val coordinates = StringBuffer()
            .append(latLong?.latitude)
            .append(" , ")
            .append(latLong?.longitude)
            .toString()
        binding?.edtPropertyPropertyCoordinatesValue?.setText(coordinates)
        binding?.fabCancel?.setOnClickListener {
            val latLong = binding?.edtPropertyPropertyCoordinatesValue?.text.toString()
            if (latLong.isNotEmpty()) {
                binding?.edtPropertyPropertyCoordinatesValue?.setText("")
                binding?.edtPropertyValue?.setText("")
            } else {
                dismissAllowingStateLoss()
            }
        }
        binding?.btnSubmit?.setOnClickListener {
            val title = binding?.edtPropertyValue?.text.toString()
            val latLong = binding?.edtPropertyPropertyCoordinatesValue?.text.toString()
            if (title.isNotEmpty() && latLong.isNotEmpty()) {
                locationAddMarkerViewModel.insert(
                    LocationEntry(
                        propertyName = title,
                        coordinates = latLong
                    )
                )
            } else {
                Toast.makeText(requireActivity(), getString(R.string.enter_data), Toast.LENGTH_LONG)
                    .show()
            }
        }

        observeData()
    }

    private fun observeData() {
        locationAddMarkerViewModel.getIsInserted().observe(this) { success ->
            val title = binding?.edtPropertyValue?.text.toString()
            if (success) {
                val bundle = Bundle().apply {
                    putSerializable(BUNDLE_KEY_ENTRY_MODEL, UiLocationEntry(title, latLong))
                }
                parentFragmentManager.setFragmentResult(BUNDLE_KEY_ENTRY, bundle)
                Toast.makeText(requireActivity(), buildString {
                    append(title)
                    append(getString(R.string.save_in_database))
                }, Toast.LENGTH_LONG).show()
                dismissAllowingStateLoss()
            }
        }
    }


    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_KEY_LAT_LONG = "LatLong"
        const val BUNDLE_KEY_ENTRY_MODEL = "EntryModel"
        const val BUNDLE_KEY_ENTRY = "Entry"
        fun newInstance(latLong: LatLng): LocationAddMarkerFragment =
            LocationAddMarkerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_LAT_LONG, latLong)
                }
            }
    }
}