package com.example.afaq.presentation.alarm.manager

import com.example.afaq.data.alarm.AlertRepo
import com.example.afaq.data.alarm.model.AlertEntity
import com.example.afaq.services.alarms.AndroidAlarmManager
import com.example.afaq.services.workmanager.WorkManagerScheduler
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test


class AlertViewModelTest {
    @MockK
    private lateinit var repo: AlertRepo

    @MockK
    private lateinit var alarmManager: AndroidAlarmManager

    @MockK
    private lateinit var workManagerScheduler: WorkManagerScheduler
    private lateinit var viewModel: AlertViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        every { repo.getAllAlerts() } returns flowOf(emptyList())
        viewModel = AlertViewModel(
            repo,
            alarmManager,
            workManagerScheduler
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun addAlert_typeAlarm_schedulesAlarm() = runTest {
        // Arrange
        val dummy = createDummyAlertEntity() // type = "ALARM"
        val savedEntity = dummy.copy(id = 1)

        coEvery { repo.insertAlert(any()) } returns 1L
        every { alarmManager.schedule(savedEntity) } just runs

        // Act
        viewModel.addAlert(
            startTime = dummy.startTime,
            endTime = dummy.endTime,
            type = dummy.type
        )

        advanceUntilIdle()
        // Assert
        assertThat(viewModel.addState.value, `is`(AddAlarmState.Idle))
        coVerify { repo.insertAlert(any()) }
        verify { alarmManager.schedule(savedEntity) }
    }

    @Test
    fun addAlert_typeNotification_schedulesWorkManager() = runTest {
        // Arrange
        val dummy = createDummyAlertEntity().copy(type = "NOTIFICATION")
        val savedEntity = dummy.copy(id = 1)

        coEvery { repo.insertAlert(any()) } returns 1L
        every { workManagerScheduler.schedule(savedEntity) } just runs

        // Act
        viewModel.addAlert(
            startTime = dummy.startTime,
            endTime = dummy.endTime,
            type = dummy.type
        )

        advanceUntilIdle()
        // Assert
        assertThat(viewModel.addState.value, `is`(AddAlarmState.Idle))
        verify { workManagerScheduler.schedule(savedEntity) }
        verify(exactly = 0) { alarmManager.schedule(any()) }
    }

    @Test
    fun addAlert_whenRepoFails_emitsErrorState() = runTest {
        // Arrange
        val dummy = createDummyAlertEntity()

        coEvery {
            repo.insertAlert(any())
        } throws Exception("Database error")

        // Act
        viewModel.addAlert(
            startTime = dummy.startTime,
            endTime = dummy.endTime,
            type = dummy.type
        )
        advanceUntilIdle()

        // Assert
        assertThat(
            viewModel.addState.value,
            `is`(AddAlarmState.Error("Database error"))
        )
    }

    @Test
    fun addAlert_initially_emitsLoadingState() = runTest {
        // Arrange
        val dummy = createDummyAlertEntity()
        coEvery { repo.insertAlert(any()) } coAnswers {
            delay(1000)
            1L
        }
        every { alarmManager.schedule(any()) } just runs

        // Act
        viewModel.addAlert(
            startTime = dummy.startTime,
            endTime = dummy.endTime,
            type = dummy.type
        )

        // Assert - loading state before completion
        assertThat(viewModel.addState.value, `is`(AddAlarmState.Loading))
        advanceUntilIdle()
    }

    @Test
    fun deleteAlarm_whenAlarmEntityGiven_deleteFromDBAndCanel() = runTest {
        // Arrange
        val dummy = createDummyAlertEntity() // type = "ALARM"
        coEvery { repo.deleteAlertById(dummy.id) } just runs
        every { alarmManager.cancel(dummy) } just runs

        viewModel.deleteAlert(dummy)

        advanceUntilIdle()

        verify { alarmManager.cancel(dummy) }
        verify(exactly = 0) { workManagerScheduler.cancel(dummy.id) }

    }

    @Test
    fun deleteAlert_whenTypeNotification_cancelsWorkManager() = runTest {
        // Arrange
        val dummy = createDummyAlertEntity().copy(type = "NOTIFICATION")

        coEvery { repo.deleteAlertById(dummy.id) } just runs
        every { workManagerScheduler.cancel(dummy.id) } just runs

        // Act
        viewModel.deleteAlert(dummy)
        advanceUntilIdle()

        // Assert
        coVerify { repo.deleteAlertById(dummy.id) }
        verify { workManagerScheduler.cancel(dummy.id) }
        verify(exactly = 0) { alarmManager.cancel(any()) }
    }

    private fun createDummyAlertEntity(): AlertEntity {
        return AlertEntity(
            id = 1,
            startTime = System.currentTimeMillis() + 3600000L, // 1 hour from now
            endTime = System.currentTimeMillis() + 7200000L,   // 2 hours from now
            type = "ALARM",
            isActive = true
        )
    }
}