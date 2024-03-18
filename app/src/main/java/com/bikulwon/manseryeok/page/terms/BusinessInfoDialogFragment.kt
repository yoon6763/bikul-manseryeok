package com.bikulwon.manseryeok.page.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bikulwon.manseryeok.databinding.FragmentBusinessInfoDialogBinding

class BusinessInfoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentBusinessInfoDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessInfoDialogBinding.inflate(inflater, container, false)

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    companion object {
        fun newInstance() = BusinessInfoDialogFragment()
    }
}