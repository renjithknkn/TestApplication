package com.llyod.directory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.llyod.directory.utils.MainDispatcherRule
import org.junit.Rule

open class BaseUnitTest {
    @get:Rule
    val corutinteTestRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}