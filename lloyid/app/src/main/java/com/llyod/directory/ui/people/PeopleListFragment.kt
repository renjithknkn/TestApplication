package com.llyod.directory.ui.people


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.llyod.directory.R
import com.llyod.directory.databinding.FragmentPeoplelistBinding

import com.llyod.directory.repository.models.People
import com.llyod.directory.ui.common.DialogHelper
import com.llyod.directory.ui.people.adapters.PeopleListAdapter
import com.llyod.directory.viewmodels.PeopleListViewModel

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PeopleListFragment : Fragment() {

    val viewmodel by activityViewModels<PeopleListViewModel>()

    @Inject
    lateinit var dialogHelper: DialogHelper

    @Inject
    lateinit var peopleListAdapter: PeopleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewBiding = FragmentPeoplelistBinding.inflate(inflater, container, false)


        setupViewStates(viewBiding)

        viewmodel.loadPeoplesList()



        return viewBiding.root
    }

    private fun setupViewStates(viewBiding: FragmentPeoplelistBinding) {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.peopleListErrorMessage.collect { errorMessage ->
                if (errorMessage.isEmpty()) {
                    dialogHelper?.dismissDialog()
                } else {
                    dialogHelper?.showDialog(errorMessage)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.lisProgressBardUiState.collect {
                if (it) {
                    Log.d("LOGGG","SETTING THE VISBILITY TRUE")
                    viewBiding.progressBar.visibility = View.VISIBLE
                } else {
                    Log.d("LOGGG","SETTING THE VISBILITY FALSE")
                    viewBiding.progressBar.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.peopleListUiState.collect {
                setupList(viewBiding.peopleList as RecyclerView, it)
            }
        }
    }

    private fun setupList(
        recyclerView: RecyclerView,
        peopleList: List<People>
    ) {

        val columnCount = resources.getInteger(R.integer.column_count)

        //mRecycler.setLayoutManager(GridLayoutManager(mContext, columns))

        with(recyclerView) {
            layoutManager = if (columnCount == 1) {
                LinearLayoutManager(context)
            } else {
                GridLayoutManager(context, columnCount)
            }

            adapter =   peopleListAdapter.apply {
                addListToItems(peopleList)
                setListener { emmployeeId ->
                    val actoin =
                        PeopleListFragmentDirections.actionPeopleListFragmentToPeopleDetailFragment(
                            emmployeeId
                        )
                    findNavController().navigate(actoin)
                }

            }

        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = PeopleListFragment()
    }
}