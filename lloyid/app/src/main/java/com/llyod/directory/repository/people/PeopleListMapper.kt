package com.llyod.directory.repository.people

import com.llyod.directory.repository.models.People
import com.llyod.directory.repository.models.PeopleRaw
import javax.inject.Inject

class PeopleListMapper  @Inject constructor() :Function1<List<PeopleRaw>,List<People>>{

    override fun invoke(rawPopleListLIst: List<PeopleRaw>): List<People> {
        return rawPopleListLIst.map {
            People(it.id,
                it.firstName+" "+it.lastName,
                it.jobtitle,
                it.avatar,
                it.email,
                it.favouriteColor
            )
        }
    }

}
