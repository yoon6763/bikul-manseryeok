package com.example.manseryeok.page.compass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.manseryeok.R
import com.example.manseryeok.databinding.FragmentMapSearchBinding


class MapSearchFragment : Fragment() {

    interface OnSearchButtonClickListener {
        fun onSearchButtonClick(latitude: Double, longitude: Double)
    }

    var onSearchButtonClickListener: OnSearchButtonClickListener? = null

    private lateinit var binding: FragmentMapSearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapSearchBinding.inflate(inflater, container, false)

        binding.etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(context, binding.etSearch.text.toString(), Toast.LENGTH_SHORT).show()

                return@setOnEditorActionListener true
            }
            false
        }



        return binding.root
    }

    companion object {
        fun newInstance() = MapSearchFragment()
    }
}