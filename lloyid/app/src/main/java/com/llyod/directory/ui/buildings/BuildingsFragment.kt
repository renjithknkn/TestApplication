package com.llyod.directory.ui.buildings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.llyod.directory.R
import com.llyod.directory.databinding.FragmentBuildingsBinding
import com.llyod.directory.repository.models.Building
import com.llyod.directory.ui.common.DialogHelper
import com.llyod.directory.ui.buildings.adapters.BuildingListAdapter
import com.llyod.directory.viewmodels.BuildingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BuildingsFragment : Fragment() {


    val viewmodel by activityViewModels<BuildingViewModel>()

    @Inject
    lateinit var dialogHelper: DialogHelper

    @Inject
    lateinit var buildingListAdapter: BuildingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBiding = FragmentBuildingsBinding.inflate(inflater, container, false)


        setupViewStates(viewBiding)

        viewmodel.loadBuildingsList()

        return viewBiding.root
    }

    private fun setupViewStates(viewBiding: FragmentBuildingsBinding) {


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.buildingListErrorMessage.collect { errorMessage ->
                if (errorMessage.isEmpty()) {
                    dialogHelper?.dismissDialog()
                } else {
                    dialogHelper?.showDialog(errorMessage)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.buildingListLoader.collect {
                if (it) {
                    viewBiding.progressBar.visibility = View.VISIBLE
                } else {
                    viewBiding.progressBar.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.buildingsList.collect {
                setupList(viewBiding.roomsList, it)
            }
        }
    }

    private fun setupList(
        recyclerView: RecyclerView,
        peopleList: List<Building>
    ) {
        val columnCount = resources.getInteger(R.integer.column_count)
        with(recyclerView) {
            layoutManager = if(columnCount==1){ LinearLayoutManager(context)} else{
                GridLayoutManager(context,columnCount)
            }
            adapter = buildingListAdapter.apply {
                addListToItems(peopleList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BuildingsFragment()
    }
}