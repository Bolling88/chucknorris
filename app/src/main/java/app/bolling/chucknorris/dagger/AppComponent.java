package app.bolling.chucknorris.dagger;

import javax.inject.Singleton;

import app.bolling.chucknorris.DataRepository;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface AppComponent {
    void inject(DataRepository repository);
}