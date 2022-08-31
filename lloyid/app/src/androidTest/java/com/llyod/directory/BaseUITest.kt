package com.llyod.directory


import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.llyod.directory.di.idlingResource

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
abstract class BaseUITest {

    @Before
    fun init() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun strip(){
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}