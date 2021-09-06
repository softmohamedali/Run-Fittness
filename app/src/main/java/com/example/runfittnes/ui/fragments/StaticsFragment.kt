package com.example.runfittnes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.runfittnes.R
import com.example.runfittnes.databinding.FragmentSetupBinding
import com.example.runfittnes.databinding.FragmentStaticsBinding
import com.example.runfittnes.utils.MyUtility
import com.example.runfittnes.viewmodels.StaticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round


@AndroidEntryPoint
class StaticsFragment : Fragment() {
    private var _binding: FragmentStaticsBinding?=null
    private val binding get() = _binding!!

    private val mviewMdel:StaticsViewModel by viewModels<StaticsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentStaticsBinding.inflate(layoutInflater)
        observeAll()
        return binding.root
    }

    private fun observeAll()
    {
        mviewMdel.totalAvgSpeed.observe(viewLifecycleOwner){
            binding.tvAvgspeedStaticfrag.text= ((round(it)*10)/10).toString()
        }
        mviewMdel.totalRunTime.observe(viewLifecycleOwner){
            binding.tvTimerunStaticfrag.text=MyUtility.formatTime(it)
        }
        mviewMdel.totalDistence.observe(viewLifecycleOwner){
            binding.tvDistenceStaticfrag.text="${(it/1000)}km"
        }
        mviewMdel.totalCalBurned.observe(viewLifecycleOwner){
            binding.tvClburnedStaticfrag.text=it.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}