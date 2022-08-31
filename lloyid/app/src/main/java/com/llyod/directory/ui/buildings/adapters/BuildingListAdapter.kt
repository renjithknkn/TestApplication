package com.llyod.directory.ui.buildings.adapters

import android.content.Context
import android.os.Build

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.llyod.directory.R
import com.llyod.directory.databinding.BuildingListItemBinding
import com.llyod.directory.repository.models.Building
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BuildingListAdapter @Inject constructor(
) : RecyclerView.Adapter<BuildingListAdapter.ViewHolder>() {

    private var values: ArrayList<Building> = ArrayList()
    private lateinit var context: Context


    fun addListToItems(buildingList: List<Building>){
        if(buildingList.isNotEmpty()){
            values = buildingList as ArrayList<Building>
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            BuildingListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = values[position]
        holder.roomId.text = context.getString(R.string.building_id,room.id)
        holder.maxOccupancy.text =context.getString(R.string.building_occupancy,room.maxOccupancy)

        holder.isOccupiedStatus.text =  context.getString(R.string.building_occupied_status,
                                                 if(room.isOccupied){""}else{"not"})

        if(room.isOccupied){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.root.setCardBackgroundColor(context.getColor(R.color.rose_800))
            }else{
                holder.root.setCardBackgroundColor(context.resources.getColor(R.color.rose_800))
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.root.setCardBackgroundColor(context.getColor(R.color.green_500))
            }else{
                holder.root.setCardBackgroundColor(context.resources.getColor(R.color.green_500))
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: BuildingListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val roomId: TextView = binding.roomId
        val maxOccupancy: TextView = binding.maxOccupancy
        val isOccupiedStatus : TextView = binding.isOccupied
        val root : CardView = binding.root
    }

}