package app.bolling.chucknorris;

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
    private long lastInsertedId;

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
     * Will merge the two observables and then call onComplete
     *
     * @return an observable
     */
    public Flowable<JokeEntity> getJoke() {
        //just the the first one to return. Should be the DB, if the DB have any value
        return mDatabase.comicDao().getUnreadJokes();
    }

    private void fetchJokeFromApi() {
        TwitterApi service = retorfit.create(TwitterApi.class);
        service.getJoke()
                .subscribeOn(Schedulers.io())
                .subscribe(joke -> lastInsertedId = mDatabase.comicDao().insert(joke));
    }

    public void saveJoke(JokeEntity joke) {
        Observable.just(joke)
                .observeOn(Schedulers.io())
                .doOnNext(jokeEntity -> mDatabase.comicDao().insert(jokeEntity))
                .subscribe();
    }

    public void loadNewJoke() {
        fetchJokeFromApi();
    }

    private interface TwitterApi {
        @GET("/jokes/random")
        Observable<JokeEntity> getJoke();
    }
}
