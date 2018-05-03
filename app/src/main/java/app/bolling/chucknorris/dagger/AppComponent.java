package app.bolling.chucknorris.dagger;

import javax.inject.Singleton;

import app.bolling.chucknorris.DataRepository;
import app.bolling.chucknorris.ui.favourite.FavouriteViewModel;
import app.bolling.chucknorris.ui.favourite.FavouritesFragment;
import app.bolling.chucknorris.ui.joke.JokeFragment;
import app.bolling.chucknorris.ui.joke.JokeViewModel;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RoomModule.class})
public interface AppComponent {
    void inject(DataRepository repository);
    void inject(JokeFragment fragment);
    void inject(JokeViewModel viewModel);
    void inject(FavouritesFragment fragment);
    void inject(FavouriteViewModel viewModel);
}