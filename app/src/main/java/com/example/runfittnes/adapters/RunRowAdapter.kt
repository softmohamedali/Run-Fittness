package com.example.runfittnes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runfittnes.R
import com.example.runfittnes.data.entity.Run
import com.example.runfittnes.utils.MyUtility

class RunRowAdapter:RecyclerView.Adapter<RunRowAdapter.VH>() {
    inner class VH(item: View):RecyclerView.ViewHolder(item){

    }

    private var differCallBack=object :DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode()==newItem.hashCode()
        }

    }

    private val differ=AsyncListDiffer(this,differCallBack)

    fun setData(data:List<Run>)=differ.submitList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val myView=LayoutInflater.from(parent.context).inflate(R.layout.run_row_recy_item,parent,false)
        return VH(myView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        var run=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(findViewById(R.id.img_mapimg_rowitem))
            findViewById<TextView>(R.id.tv_avgspeed_rowitem).text="${run.avgSpeed}km/s"
            findViewById<TextView>(R.id.tv_timestart_rowitem).text="${run.timeStart!!} S"
            findViewById<TextView>(R.id.tv_burned_rowitem).text="${run.caloryBurned}clu"
            findViewById<TextView>(R.id.tv_distence_rowitem).text="${(run.distence!!)/1000F}km"
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}