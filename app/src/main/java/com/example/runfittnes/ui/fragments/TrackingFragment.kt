package com.example.runfittnes.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.runfittnes.R
import com.example.runfittnes.databinding.FragmentSetupBinding
import com.example.runfittnes.databinding.FragmentTrackingBinding
import com.example.runfittnes.services.TrackServices
import com.example.runfittnes.utils.Constants
import com.example.runfittnes.utils.MyUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.web3j.protocol.Web3jService
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.core.methods.response.Web3Sha3
import org.web3j.protocol.rx.Web3jRx

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding?=null
    private val binding get() = _binding!!

    private var googleMap:GoogleMap?=null

    private var isTracking:Boolean=false

    private var trakingPath= mutableListOf<MutableList<LatLng>>()

    private var timeinMls=0L

    private var mmenu:Menu?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTrackingBinding.inflate(layoutInflater)
        binding.mapTrackfrag.onCreate(savedInstanceState)


        binding.mapTrackfrag.getMapAsync {
            googleMap=it
            addAllPolyLine()
        }

        binding.btnTrackfragStart.setOnClickListener {
            toggleState()
        }

        setUpObserver()


        return binding.root
    }



    private fun setUpObserver()
    {
        TrackServices.isTracking.observe(viewLifecycleOwner,{
            isTracking=it
            updataTracking(isTracking)
        })

        TrackServices.trackPath.observe(viewLifecycleOwner,{
            trakingPath=it
            addLastPathPointToMap()
            setCameraPositionInTheLast()
        })

        TrackServices.timeInMilles.observe(viewLifecycleOwner,{
            timeinMls=it
            val time=MyUtility.formatTime(timeinMls,true)
            binding.tvTimerTrackfrag.text=time
        })
    }


    private fun sendActionToTrackingServices(action:String)=
        Intent(requireContext(),TrackServices::class.java).also {
            it.action=action
            requireContext().startService(it)
        }

    private fun toggleState()
    {
        if (isTracking)
        {
            mmenu?.getItem(0)?.isVisible=true
            sendActionToTrackingServices(Constants.SERVICES_ACTION_PAUSE)
        }else{
            sendActionToTrackingServices(Constants.SERVICES_ACTION_START_OR_RESUME)
        }
    }

    private fun updataTracking(istrack:Boolean)
    {
        isTracking=istrack
        if (!isTracking)
        {
            binding.btnTrackfragStart.text="Start"
        }else
        {
            mmenu?.getItem(0)?.isVisible=true
            binding.btnTrackfragStart.text="Stop"
        }
    }

    fun cancleTracking(va:() -> Unit)
    {
        showcanclingDialog()
    }

    private fun showcanclingDialog()
    {
        val alertDialog=MaterialAlertDialogBuilder(requireContext())
                .setMessage("Are  you sure you went to cancle tracking")
                .setTitle("Cancle Run !")
                .setIcon(R.drawable.ic_delte)
                .setPositiveButton("yes"){ _ ,_ ->
//                    sendActionToTrackingServices(Constants.SERVICES_ACTION_STOP)
                    stpRun()
                }
                .setNegativeButton("No"){dia ,_ ->
                    dia.cancel()
                }.create()
        alertDialog.show()
    }

    private fun stpRun() {
        sendActionToTrackingServices(Constants.SERVICES_ACTION_STOP)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }


    private fun setCameraPositionInTheLast() {
        if (trakingPath.isNotEmpty() && trakingPath.last().isNotEmpty())
        {
            googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(trakingPath.last().last(),15f)
            )
        }
    }


    /*
    *this fun will call evry time we create or recreate mapview beacuse if we rotate mobile this
    * fun will redraw tha ployline again from the start
     */
    private fun addAllPolyLine(){
        for (polyline in trakingPath){
            val polylineOption=PolylineOptions()
                    .color(R.color.green)
                    .width(8f)
                    .addAll(polyline)
            googleMap?.addPolyline(polylineOption)
        }
    }

    private fun addLastPathPointToMap() {
        if (trakingPath.isNotEmpty() && trakingPath.last().size > 1)
        {
            val secondLastlatlng=trakingPath.last()[trakingPath.last().size-2]
            val lastLatLng=trakingPath.last().last()
            val polylineOption=PolylineOptions()
                    .color(R.color.red)
                    .width(12f)
                    .add(secondLastlatlng)
                    .add(lastLatLng)
            googleMap?.addPolyline(polylineOption)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mnue_rtack_frag_cancle,menu)
        this.mmenu=menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (timeinMls > 0)
        {
            this.mmenu?.getItem(0)?.isVisible=true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.menue_item_canle ->{
                cancleTracking {  }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onStart() {
        super.onStart()
        binding.mapTrackfrag.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapTrackfrag.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.mapTrackfrag.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapTrackfrag.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapTrackfrag.onLowMemory()
    }
}