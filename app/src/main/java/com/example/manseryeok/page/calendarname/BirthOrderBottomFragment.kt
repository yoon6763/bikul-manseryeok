package com.example.manseryeok.page.calendarname

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.manseryeok.databinding.FragmentBirthOrderBottomFragmentBinding


class BirthOrderBottomFragment : BottomSheetDialogFragment() {

    interface OnBirthDisplayAscBottomFragmentListener {
        fun onOrderSelect(isAsc: Boolean)
    }

    var onBirthDisplayAscBottomFragmentListener: OnBirthDisplayAscBottomFragmentListener? = null
    private var binding: FragmentBirthOrderBottomFragmentBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBirthOrderBottomFragmentBinding.inflate(inflater, container, false)

        binding?.run {

            rgBirthDisplayAscBottom.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    rbBirthDisplayAsc.id -> {
                        onBirthDisplayAscBottomFragmentListener?.onOrderSelect(true)
                        dismiss()
                    }

                    rbBirthDisplayDesc.id -> {
                        onBirthDisplayAscBottomFragmentListener?.onOrderSelect(false)
                        dismiss()
                    }
                }
            }

            rbBirthDisplayAsc.setOnClickListener {
                onBirthDisplayAscBottomFragmentListener?.onOrderSelect(true)
                dismiss()
            }

            rbBirthDisplayDesc.setOnClickListener {
                onBirthDisplayAscBottomFragmentListener?.onOrderSelect(false)
                dismiss()
            }
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}