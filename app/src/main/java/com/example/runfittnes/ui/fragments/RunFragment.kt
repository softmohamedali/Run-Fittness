package com.example.runfittnes.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runfittnes.R
import com.example.runfittnes.adapters.RunRowAdapter
import com.example.runfittnes.databinding.FragmentRunBinding
import com.example.runfittnes.utils.Constants
import com.example.runfittnes.utils.MyUtility
import com.example.runfittnes.utils.SortedBy
import com.example.runfittnes.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment() ,EasyPermissions.PermissionCallbacks {

    private var _binding:FragmentRunBinding?=null
    private val binding get() = _binding!!
    private val mviewModel by viewModels<MainViewModel>()
    private val madpater by lazy { RunRowAdapter() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding= FragmentRunBinding.inflate(layoutInflater)

        requstREquiredPermission()
        setUpviews()
        setRecy()
        when(mviewModel.sortedBy){
            SortedBy.DISTENCE ->{binding.aetRuntrackSortby.setSelection(1)}
            SortedBy.DATA ->{binding.aetRuntrackSortby.setSelection(0)}
            SortedBy.AVGSPEED ->{binding.aetRuntrackSortby.setSelection(2)}
            SortedBy.TIMERUN ->{binding.aetRuntrackSortby.setSelection(4)}
            SortedBy.CALBURNED ->{binding.aetRuntrackSortby.setSelection(3)}
        }
        binding.aetRuntrackSortby.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position)
                    {
                        0 ->{mviewModel.sortBy(SortedBy.DATA)}
                        1 ->{mviewModel.sortBy(SortedBy.DISTENCE)}
                        2 ->{mviewModel.sortBy(SortedBy.AVGSPEED)}
                        3 ->{mviewModel.sortBy(SortedBy.CALBURNED)}
                        4 ->{mviewModel.sortBy(SortedBy.TIMERUN)}
                    }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        mviewModel.runs.observe(viewLifecycleOwner,{
            madpater.setData(it)
        })

        binding.fabRunfragAddtrack.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }


        return binding.root
    }

    private fun setUpviews()
    {
        val Items = listOf("Data","Distence", "AVG speed","cla Burned","time run")
        val Adapter = ArrayAdapter(requireContext(), R.layout.item_sort_row_item, Items)
        binding.aetRuntrackSortby.setAdapter(Adapter)
    }
    private fun setRecy()
    {
        binding.recyRunfrag.apply {
            adapter=madpater
            layoutManager=LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    fun requstREquiredPermission(){
        if (MyUtility.hasRequirePermission(requireContext())){
            return
        }else{
            if (Build.VERSION.SDK_INT< Build.VERSION_CODES.Q)
            {
                EasyPermissions.requestPermissions(
                        this,
                        "you need this permission to work tjis app",
                        Constants.REQUSTCODE_PERMISSION_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                )
            }
            else{
                EasyPermissions.requestPermissions(
                        this,
                        "you need this permission to work tjis app",
                        Constants.REQUSTCODE_PERMISSION_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms))
        {
            AppSettingsDialog.Builder(this).build().show()
        }
        else{
            requstREquiredPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

}