package com.llyod.directory.ui.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.llyod.directory.R

import com.llyod.directory.databinding.FragmentPeopleDetailBinding
import com.llyod.directory.repository.models.People
import com.llyod.directory.ui.common.DialogHelper
import com.llyod.directory.viewmodels.PeopleListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PeopleDetailFragment : Fragment() {

    val args : PeopleDetailFragmentArgs by navArgs()
    val viewmodel by activityViewModels<PeopleListViewModel>()

    @Inject
    lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val employeeId = args.employeeId
        val viewBiding = FragmentPeopleDetailBinding.inflate(inflater, container, false)


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.peopleDetail.collect {
                setValues(viewBiding,it)
            }
        }

        viewmodel.loadPeopleDetail(employeeId)
        return viewBiding.root
    }


    private fun setValues(viewBiding: FragmentPeopleDetailBinding, people: People) {
        people.let {
            viewBiding.peopleName.text = people.name
            viewBiding.jobTitle.text = people.jobTitle
            viewBiding.employeeId.text = getString(R.string.label_emp_id_is,people.employeeId)
            viewBiding.email.text = people.email
            viewBiding.favColor.text = getString(R.string.label_emp_fav_color_is,people.favColor)

            Glide
                .with(viewBiding.avatar.context)
                .load(people.avatar)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(viewBiding.avatar);

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PeopleDetailFragment()
    }
}