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

    private Retrofit retrofit;

    private final AppDatabase mDatabase;
    private long lastInsertedId;
    private Flowable newJokeFlowable;

    @Inject
    public DataRepository(AppDatabase database, Retrofit retorfit) {
        ChuckApp.component.inject(this);
        mDatabase = database;
        this.retrofit = retorfit;
    }

    /**
     * Will merge the two observables and then call onComplete
     *
     * @return an observable
     */
    public Flowable<JokeEntity> getJoke(String jokeId) {
        //just the the first one to return. Should be the DB, if the DB have any value
        return mDatabase.jokeDao().getJoke(jokeId)
                .subscribeOn(Schedulers.io());
    }

    public void saveJoke(JokeEntity joke) {
        Observable.just(joke)
                .observeOn(Schedulers.io())
                .doOnNext(jokeEntity -> mDatabase.jokeDao().insert(jokeEntity))
                .subscribe();
    }

    public Observable<JokeEntity> loadNewJoke() {
        TwitterApi service = retrofit.create(TwitterApi.class);
        return service.getJoke()
                .subscribeOn(Schedulers.io())
                .doOnNext(entity -> lastInsertedId = mDatabase.jokeDao().insert(entity));
    }

    private interface TwitterApi {
        @GET("/jokes/random")
        Observable<JokeEntity> getJoke();
    }
}
