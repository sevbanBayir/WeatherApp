package com.sevban.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.sevban.common.model.ErrorType
import com.sevban.common.model.Failure
import com.sevban.detail.mapper.toForecastUiModel
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.testing.extension.MainCoroutineExtension
import com.sevban.testing.testdata.dummyForecast
import com.sevban.testing.testdata.serverError
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class DetailViewModelTest {
    private lateinit var viewModel: DetailViewModel
    private lateinit var getForecastUseCase: GetForecastUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeEach
    fun setUp() {
        getForecastUseCase = mockk()
        savedStateHandle = SavedStateHandle()
        viewModel = DetailViewModel(getForecastUseCase, savedStateHandle)
    }

    @Test
    fun `given viewModel when initialized then forecastState should be Loading`() = runTest {
        assertThat(viewModel.forecastState.value).isEqualTo(ForecastState.Loading)
    }

    @Test
    fun `given latitude and longitude when forecast is fetched then forecastState should be Success`() =
        runTest {
            // Given
            val latitude = 40.7128
            val longitude = -74.0060

            savedStateHandle[DetailViewModel.LATITUDE_ARG] = latitude
            savedStateHandle[DetailViewModel.LONGITUDE_ARG] = longitude

            // When
            every { getForecastUseCase.execute(latitude.toString(), longitude.toString()) } returns flow {
                delay(10) // Ensure time for initial Loading emission
                emit(dummyForecast)
            }

            // Then
            viewModel.forecastState.test {
                val firstItem = awaitItem()
                assertThat(firstItem).isEqualTo(ForecastState.Loading)
                val secondItem = awaitItem()
                assertThat(secondItem).isEqualTo(ForecastState.Success(dummyForecast.toForecastUiModel()))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given forecast fetching fails when executed then forecastState should be Error`() = runTest {
        val latitude = 40.7128
        val longitude = -74.0060

        savedStateHandle[DetailViewModel.LATITUDE_ARG] = latitude
        savedStateHandle[DetailViewModel.LONGITUDE_ARG] = longitude

        every { getForecastUseCase.execute(latitude.toString(), longitude.toString()) } throws serverError
        viewModel.forecastState.test {
            val firstItem = awaitItem()
            assertThat(firstItem).isEqualTo(ForecastState.Error(serverError))
        }
    }


    @Test
    fun `given OnTryAgainClick event when triggered then retryTrigger should emit unit`() = runTest {

        viewModel.retryTrigger.test {
            viewModel.onEvent(DetailScreenEvent.OnTryAgainClick)
            assertThat(awaitItem()).isEqualTo(Unit)
            expectNoEvents()
        }
    }

    @Test
    fun `given savedStateHandle with null values when fetching forecast then state should remain Loading`() = runTest {
        savedStateHandle[DetailViewModel.LATITUDE_ARG] = null
        savedStateHandle[DetailViewModel.LONGITUDE_ARG] = null

        every { getForecastUseCase.execute(any(), any()) } returns flowOf(dummyForecast)

        viewModel.forecastState.test {
            val firstItem = awaitItem()
            assertThat(firstItem).isEqualTo(ForecastState.Loading)
            expectNoEvents()
        }
    }

    @Test
    fun `given valid lat long when getForecastUseCase is called then forecastState should be updated`() = runTest {
        val latitude = 40.7128
        val longitude = -74.0060

        savedStateHandle[DetailViewModel.LATITUDE_ARG] = latitude
        savedStateHandle[DetailViewModel.LONGITUDE_ARG] = longitude

        every { getForecastUseCase.execute(latitude.toString(), longitude.toString()) } returns flow {
            delay(10)
            emit(dummyForecast)
        }

        viewModel.forecastState.test {
            val firstItem = awaitItem()
            assertThat(firstItem).isEqualTo(ForecastState.Loading)
            val secondItem = awaitItem()
            assertThat(secondItem).isEqualTo(ForecastState.Success(dummyForecast.toForecastUiModel()))
            verify { getForecastUseCase.execute(latitude.toString(), longitude.toString()) }
        }
    }


    @Test
    fun `given retryTrigger when triggered then forecastState should reload data`()  = runTest {
        val latitude = 40.7128
        val longitude = -74.0060
        savedStateHandle[DetailViewModel.LATITUDE_ARG] = latitude
        savedStateHandle[DetailViewModel.LONGITUDE_ARG] = longitude
        every { getForecastUseCase.execute(latitude.toString(), longitude.toString()) } returns flow {
            delay(10)
            emit(dummyForecast)
        }

        viewModel.forecastState.test {
            val firstItem = awaitItem()
            assertThat(firstItem).isEqualTo(ForecastState.Loading)
            val secondItem = awaitItem()
            assertThat(secondItem).isEqualTo(ForecastState.Success(dummyForecast.toForecastUiModel()))
            viewModel.onEvent(DetailScreenEvent.OnTryAgainClick)
            val thirdItem = awaitItem()
            assertThat(thirdItem).isEqualTo(ForecastState.Loading)
            val fourthItem = awaitItem()
            assertThat(fourthItem).isEqualTo(ForecastState.Success(dummyForecast.toForecastUiModel()))
            verify(exactly = 2) { getForecastUseCase.execute(latitude.toString(), longitude.toString()) }
        }
    }


}