package app.bolling.chucknorris;

import java.util.List;

import javax.inject.Inject;

import app.bolling.chucknorris.database.AppDatabase;
import app.bolling.chucknorris.database.model.JokeEntity;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static final String TAG = "DataRepository";
    private static DataRepository sInstance;

    @Inject
    Retrofit retorfit;

    private final AppDatabase mDatabase;

    private DataRepository(final AppDatabase database) {
        BasicApp.component.inject(this);

        mDatabase = database;
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public Flowable<List<JokeEntity>> getAllQuestions() {
        return mDatabase.comicDao().getAllQuestions();
    }

    /**
     * Will merge the two observables and then call onComplete
     * @param comicId
     * @return an observable
     */
    public Observable<JokeEntity> getJoke(final int comicId) {
        //just the the first one to return. Should be the DB, if the DB have any value
        return Observable.merge(
                getJokeFromDatabase(comicId),
                getJokeFromApi()).take(1);
    }

    private Observable<JokeEntity> getJokeFromApi() {
        TwitterApi service = retorfit.create(TwitterApi.class);
        return service.getJoke()
                .subscribeOn(Schedulers.io())
                .doAfterNext((jokeEntity) -> mDatabase.comicDao().insert(jokeEntity));
    }

    private Observable<JokeEntity> getJokeFromDatabase(int comicId) {
        return mDatabase.comicDao().getJoke(comicId).toObservable().subscribeOn(Schedulers.io());
    }

    public void saveJoke(JokeEntity comic) {
        mDatabase.comicDao().insert(comic);
    }

    private interface TwitterApi {
        @GET("/jokes/random")
        Observable<JokeEntity> getJoke();
    }
}
