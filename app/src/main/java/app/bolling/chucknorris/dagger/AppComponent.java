package app.bolling.chucknorris.dagger;

import javax.inject.Singleton;

import app.bolling.chucknorris.DataRepository;
import app.bolling.chucknorris.ui.JokeFragment;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RoomModule.class})
public interface AppComponent {
    void inject(DataRepository repository);
    void inject(JokeFragment fragment);
}