package com.example.manseryeok.page.compass

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.manseryeok.databinding.FragmentCompassHelpBinding


class CompassHelpFragment : DialogFragment() {
    private val binding by lazy { FragmentCompassHelpBinding.inflate(layoutInflater) }

    companion object {
        fun newInstance(): DialogFragment {
            return CompassHelpFragment()
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.btnCompassHelpClose.setOnClickListener { dismiss() }
        return binding.root
    }
}