package app.bolling.chucknorris.ui.fragment.joke

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import app.bolling.chucknorris.DataRepository
import app.bolling.chucknorris.ResourceUtil
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations




class JokeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var app: Application
    @Mock
    lateinit var repository: DataRepository
    @Mock
    lateinit var resourceUtil: ResourceUtil

    //class that is being tested
    lateinit var jokeViewModel: JokeViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
        jokeViewModel = JokeViewModel(resourceUtil, repository, app)
    }

    @Test
    fun getObservableToast() {

    }

    @Test
    fun getLoadingVisibilityEvent() {
    }

    @Test
    fun getButtonVisibilityEvent() {
    }

    @Test
    fun getJokeChangedEvent() {
    }

    @Test
    fun onNextJokeClicked() {
        jokeViewModel.onNextJokeClicked()
    }

    @Test
    fun onFavoriteClicked() {
        jokeViewModel.onFavoriteClicked()
    }

    @Test
    fun getRepository() {
    }

    @Test
    fun setRepository() {
    }

    @Test
    fun getResources() {
    }

    @Test
    fun setResources() {
    }

    @Test
    fun getObservableToast1() {
    }

    @Test
    fun getLoadingVisibilityEvent1() {
    }

    @Test
    fun getButtonVisibilityEvent1() {
    }

    @Test
    fun getJokeChangedEvent1() {
    }

    @Test
    fun onNextJokeClicked1() {
    }

    @Test
    fun onFavoriteClicked1() {
    }
}