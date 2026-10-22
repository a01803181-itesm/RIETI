package itesm.rieti

import android.app.Application
import android.content.Context
import itesm.rieti.model.network.NetworkMonitor
import itesm.rieti.viewModel.network.NetworkVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Fake implementation of [NetworkMonitor] for unit testing.
 * Allows deterministic simulation of network connectivity transitions without hardware calls.
 */
class FakeNetworkMonitor(initialConnected: Boolean = true) : NetworkMonitor {
    private val _isConnected = MutableStateFlow(initialConnected)
    override val isConnected: Flow<Boolean> = _isConnected

    fun setConnected(connected: Boolean) {
        _isConnected.value = connected
    }
}

class TestApplicationContext(private val tempDir: File) : Application() {
    override fun getApplicationContext(): Context = this
    override fun getFilesDir(): File = tempDir
}

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkUniTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var testContext: Context

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testContext = TestApplicationContext(tempFolder.newFolder())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `NetworkVM initial state is true when monitor is online`() = runTest(testDispatcher) {
        val fakeMonitor = FakeNetworkMonitor(initialConnected = true)
        val viewModel = NetworkVM(testContext, fakeMonitor)

        val collectJob = backgroundScope.launch {
            viewModel.isNetworkAvailable.collect()
        }

        assertTrue(viewModel.isNetworkAvailable.value)
        collectJob.cancel()
    }

    @Test
    fun `NetworkVM initial state becomes false when monitor is offline`() = runTest(testDispatcher) {
        val fakeMonitor = FakeNetworkMonitor(initialConnected = false)
        val viewModel = NetworkVM(testContext, fakeMonitor)

        val collectJob = backgroundScope.launch {
            viewModel.isNetworkAvailable.collect()
        }

        assertFalse(viewModel.isNetworkAvailable.value)
        collectJob.cancel()
    }

    @Test
    fun `NetworkVM reacts to dynamic online and offline status changes`() = runTest(testDispatcher) {
        val fakeMonitor = FakeNetworkMonitor(initialConnected = true)
        val viewModel = NetworkVM(testContext, fakeMonitor)

        val collectedValues = mutableListOf<Boolean>()
        val collectJob = backgroundScope.launch {
            viewModel.isNetworkAvailable.collect {
                collectedValues.add(it)
            }
        }

        assertTrue(viewModel.isNetworkAvailable.value)

        fakeMonitor.setConnected(false)
        assertFalse(viewModel.isNetworkAvailable.value)

        fakeMonitor.setConnected(true)
        assertTrue(viewModel.isNetworkAvailable.value)

        assertEquals(listOf(true, false, true), collectedValues)
        collectJob.cancel()
    }

    @Test
    fun `NetworkVM exposes networkMonitor reference correctly`() {
        val fakeMonitor = FakeNetworkMonitor(initialConnected = true)
        val viewModel = NetworkVM(testContext, fakeMonitor)

        assertEquals(fakeMonitor, viewModel.networkMonitor)
    }
}