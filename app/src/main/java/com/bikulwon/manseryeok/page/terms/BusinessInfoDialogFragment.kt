package com.bikulwon.manseryeok.page.terms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bikulwon.manseryeok.databinding.FragmentBusinessInfoDialogBinding
import com.bikulwon.manseryeok.service.NotionAPI
import com.bikulwon.manseryeok.utils.SecretConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.awaitResponse

class BusinessInfoDialogFragment : DialogFragment() {

    private val ARG_PARAM_CONTENT = "content"
    private lateinit var binding: FragmentBusinessInfoDialogBinding
    private val notionAPI by lazy { NotionAPI.create() }
    private var content: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            content = it.getString(ARG_PARAM_CONTENT).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessInfoDialogBinding.inflate(inflater, container, false)
        binding.tvBusinessInfo.text = content
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    companion object {
        fun newInstance(content: String) = BusinessInfoDialogFragment().apply {
            this.content = content
        }
    }
}