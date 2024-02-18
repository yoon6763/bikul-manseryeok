package com.bikulwon.manseryeok.page.calendarname.popup

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.bikulwon.manseryeok.utils.TimeDiffConstants
import com.bikulwon.manseryeok.adapter.LocationAdapter
import com.bikulwon.manseryeok.databinding.FragmentLocationPickerBinding


class LocationPickerFragment : DialogFragment() {
    private val binding by lazy { FragmentLocationPickerBinding.inflate(layoutInflater) }
    private val locations = TimeDiffConstants.timeZones
    private val adapter by lazy { LocationAdapter(requireActivity(), locations) }

    lateinit var onLocationClickListener: LocationAdapter.OnLocationClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvLocation.adapter = adapter
        adapter.onLocationClickListener = onLocationClickListener
    }

    override fun onStart() {
        super.onStart()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val window: Window? = dialog!!.window
        window?.attributes = lp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter.notifyDataSetChanged()
        return binding.root
    }
}