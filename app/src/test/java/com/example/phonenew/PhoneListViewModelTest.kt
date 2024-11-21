package com.example.phonenew

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.phonenew.repositories.DataRepository
import com.example.phonenew.viewmodels.PhoneListViewModel
import com.example.realestateapp.models.Phone
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PhoneListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DataRepository

    @Mock
    private lateinit var observer: Observer<List<Phone>>

    @Test
    fun refreshPhones_returnsExpectedData() = runBlockingTest {
        val viewModel = PhoneListViewModel(repository)
        val phoneList = listOf(
            Phone(id = 1, name = "Phone 1", price = 100, image = "", credit = true),
            Phone(id = 2, name = "Phone 2", price = 200, image = "", credit = true)
        )

        `when`(repository.allPhones).thenReturn(object : LiveData<List<Phone>>() {
            init {
                postValue(phoneList)
            }
        })

        viewModel.allPhones.observeForever(observer)
        viewModel.refreshPhones()

        val liveDataValue = getValue(viewModel.allPhones)
        assertEquals(phoneList, liveDataValue)
    }

    private fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        @Suppress("UNCHECKED_CAST")
        return data[0] as T
    }
}
