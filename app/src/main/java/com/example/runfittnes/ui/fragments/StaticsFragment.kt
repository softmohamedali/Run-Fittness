package com.example.runfittnes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runfittnes.R
import com.example.runfittnes.databinding.FragmentSetupBinding
import com.example.runfittnes.databinding.FragmentStaticsBinding


class StaticsFragment : Fragment() {
    private var _binding: FragmentStaticsBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentStaticsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}