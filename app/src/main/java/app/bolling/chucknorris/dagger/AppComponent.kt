package app.bolling.chucknorris.dagger

import app.bolling.chucknorris.util.DataRepository
import app.bolling.chucknorris.ui.fragment.favourite.FavouriteViewModel
import app.bolling.chucknorris.ui.fragment.favourite.FavouritesFragment
import app.bolling.chucknorris.ui.fragment.joke.JokeFragment
import app.bolling.chucknorris.ui.fragment.joke.JokeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, RoomModule::class))
interface AppComponent {
    fun inject(repository: DataRepository)
    fun inject(fragment: JokeFragment)
    fun inject(viewModel: JokeViewModel)
    fun inject(fragment: FavouritesFragment)
    fun inject(viewModel: FavouriteViewModel)
}