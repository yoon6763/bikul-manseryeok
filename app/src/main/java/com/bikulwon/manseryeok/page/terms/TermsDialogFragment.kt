package com.bikulwon.manseryeok.page.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bikulwon.manseryeok.databinding.FragmentTermsDialogBinding
import com.bikulwon.manseryeok.utils.SharedPreferenceHelper

class TermsDialogFragment : DialogFragment() {

    interface OnDialogAgreeListener {
        fun onDialogAgree()
    }

    var onDialogAgreeListener: OnDialogAgreeListener? = null
    lateinit var binding: FragmentTermsDialogBinding

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

        val termsAgree = SharedPreferenceHelper.isTermsAgree(requireContext())
        if (termsAgree) {
            binding.btnAgree.text = "닫기"
        }

        dialog?.window?.setLayout(width, height)
    }

    companion object {
        fun newInstance() = TermsDialogFragment()
    }
}