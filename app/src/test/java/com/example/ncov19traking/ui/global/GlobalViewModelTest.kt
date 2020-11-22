package com.example.ncov19traking.ui.global

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ncov19traking.data.NCoVRepository
import com.example.ncov19traking.models.NCoVInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GlobalViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repo: NCoVRepository

    private val viewModel: GlobalViewModel

    private val defaultNCoVInfo = NCoVInfo(250, 100, 50)


    init {
        MockKAnnotations.init(this)
        viewModel = GlobalViewModel(repo)
    }

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        coEvery { repo.getAllCases() } returns defaultNCoVInfo
    }

    @Test
    fun `Given repository success response, when getAllCases(), then actual value is returned`() {

        viewModel.nCoVAllCases.observeForever {}
        val result = viewModel.nCoVAllCases.value
        assertThat(result, CoreMatchers.instanceOf(NCoVInfo::class.java))
        Assert.assertEquals(defaultNCoVInfo, result)
        coVerify(exactly = 1) { repo.getAllCases() }
    }


}