package app.bolling.chucknorris.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import app.bolling.chucknorris.database.AppDatabase;
import app.bolling.chucknorris.database.dao.JokeDao;
import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase(Application app) {
        return  Room.databaseBuilder(app, AppDatabase.class, "chuck-db").fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    JokeDao providesProductDao(AppDatabase demoDatabase) {
        return demoDatabase.jokeDao();
    }
}