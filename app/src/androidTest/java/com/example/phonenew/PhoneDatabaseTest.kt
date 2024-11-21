package com.example.realestateapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.phonenew.repositories.DataRepository
import com.example.realestateapp.database.PhoneDatabase
import com.example.realestateapp.models.Phone
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PhoneDatabaseTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var repository: DataRepository
    private lateinit var database: PhoneDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = PhoneDatabase.getDatabase(context)
        repository = DataRepository(database.phoneDao(), RetrofitClient.instance)
    }

    @Test
    fun testPhonePersistence() = runBlocking {
        val phone = Phone(id = 1, name = "Test Phone", price = 100, image = "", credit = true)
        repository.refreshPhones()
        repository.phoneDao.insertAll(listOf(phone))
        val allPhones = getValue(repository.allPhones)

        assertTrue(allPhones.contains(phone))
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
