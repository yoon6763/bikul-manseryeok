package com.bikulwon.manseryeok.page.user

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bikulwon.manseryeok.databinding.FragmentDBBottomSheetDialogBinding

class DBBottomSheetDialogFragment : BottomSheetDialogFragment() {

    interface DBSheetDialogListener {
        fun onBackupDataPressed()
        fun onLoadDataPressed()
    }

    private var _binding: FragmentDBBottomSheetDialogBinding? = null

    private val binding get() = _binding!!

    var listener: DBSheetDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDBBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnBackup.setOnClickListener {
                listener?.onBackupDataPressed()
                dismiss()
            }
            btnRestore.setOnClickListener {
                listener?.onLoadDataPressed()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}