package com.example.runfittnes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.runfittnes.R
import com.example.runfittnes.databinding.FragmentSetupBinding

class SetupFragment : Fragment(){

    private var _binding: FragmentSetupBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSetupBinding.inflate(layoutInflater)


        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}