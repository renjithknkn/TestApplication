package com.llyod.directory.ui.people.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.llyod.directory.R
import com.llyod.directory.databinding.PeopleListItemBinding
import com.llyod.directory.repository.models.People
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class PeopleListAdapter @Inject constructor(
) : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>(),Filterable {

    private lateinit var listener : (String) -> Unit
    private var values: ArrayList<People> = ArrayList()
    private  var filterdValues :  ArrayList<People> = ArrayList()
    private lateinit var context: Context

    fun addListToItems(peoplesList: List<People>){
        if(peoplesList.isNotEmpty()){
            values = peoplesList as ArrayList<People>
            filterdValues = values
        }
    }

    fun setListener(itemClickListener : (String) -> Unit){
        listener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            PeopleListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val people = filterdValues[position]
        holder.name.text = people.name
        holder.jobTitle.text = people.jobTitle
        Glide
            .with(context)
            .load(people.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.avatar);
        holder.root.setOnClickListener{
            listener(people.employeeId)
        }

    }

    override fun getItemCount(): Int = filterdValues.size

    inner class ViewHolder(binding: PeopleListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.peopleName
        val jobTitle: TextView = binding.jobTitle
        val avatar : ImageView = binding.avatar
        val root : View = binding.root
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charString = constraint?.toString() ?: ""
                var listFilter = ArrayList<People>()

                if (charString.isEmpty()) listFilter = values
                else {
                    listFilter = values.filter { people->
                        people.name.contains(charString,true)
                    } as ArrayList<People>
                }

                return FilterResults().apply { values = listFilter }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterdValues = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<People>

                notifyDataSetChanged()
            }

        }
    }

}