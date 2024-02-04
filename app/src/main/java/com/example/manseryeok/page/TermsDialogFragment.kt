package com.example.manseryeok.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.manseryeok.R
import com.example.manseryeok.databinding.FragmentTermsDialogBinding

class TermsDialogFragment : DialogFragment() {

    interface OnDialogAgreeListener {
        fun onDialogAgree()
    }

    var onDialogAgreeListener: OnDialogAgreeListener? = null
    private lateinit var binding: FragmentTermsDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTermsDialogBinding.inflate(inflater, container, false)

        binding.btnAgree.setOnClickListener {
            onDialogAgreeListener?.onDialogAgree()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, height)
    }

    companion object {
        fun newInstance() = TermsDialogFragment()
    }
}