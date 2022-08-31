package com.llyod.directory

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.interaction.BaristaDrawerInteractions.openDrawer
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.llyod.directory.di.idlingResource
import com.llyod.directory.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTestCase () : BaseUITest() {


    @get: Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun shouldDisplayPeopleList() {
        assertDisplayedAtPosition(R.id.people_list,0,R.id.people_name,"Maggie Brekke")
        assertDisplayedAtPosition(R.id.people_list,0,R.id.job_title,"Future Functionality Strategist")
        assertDisplayed(R.id.avatar)
    }

    @Test
    fun displayLoaderWhileFetchingPeopleList(){
        IdlingRegistry.getInstance().unregister(idlingResource)
        assertDisplayed(R.id.progress_bar)
    }

    @Test
    fun hideLoaderAfterFetchingPeopleList(){
        Thread.sleep(500)
        assertNotDisplayed(R.id.progress_bar)
    }

    @Test
    fun loadPeopleDetailScreenOnListItemClick(){
        clickListItem(R.id.people_list,0)
        assertDisplayed(R.id.people_detail_root)
    }
    @Test
    fun loadRoomListScreenFromMenu(){
        openDrawer();
        clickMenu(R.id.buildingsFragment);
        assertDisplayed(R.id.rooms_list)
    }


}