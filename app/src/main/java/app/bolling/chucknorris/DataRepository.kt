package app.bolling.chucknorris

import android.util.Log
import app.bolling.chucknorris.database.AppDatabase
import app.bolling.chucknorris.database.model.JokeEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject

/**
 * Repository handling the work with products and comments.
 */
class DataRepository @Inject
constructor(private val mDatabase: AppDatabase, private val retrofit: Retrofit) {

    val jokes: Flowable<List<JokeEntity>>
        get() = mDatabase.jokeDao().getJokes()

    init {
        ChuckApp.component.inject(this)
    }

    fun saveJoke(joke: JokeEntity) {
        Observable.just(joke)
                .observeOn(Schedulers.io())
                .doOnNext { jokeEntity -> mDatabase.jokeDao().insert(jokeEntity) }
                .subscribe()
    }

    fun loadNewJoke(): Observable<JokeEntity> {
        val service = retrofit.create(TwitterApi::class.java)
        return service.joke
                .subscribeOn(Schedulers.io())
    }

    fun deleteJoke(viewedJoke: JokeEntity) {
        Observable.just(viewedJoke)
                .subscribeOn(Schedulers.io())
                .doOnNext { joke -> mDatabase.jokeDao().deleteJoke(joke) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { jokeEntity -> Log.d(TAG, "Joke deleted") }
    }

    fun deleteAllNonfavouriteJokes() {
        Completable.fromAction { mDatabase.jokeDao().deleteAllNonfavouriteJokes() }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private interface TwitterApi {
        @get:GET("/jokes/random")
        val joke: Observable<JokeEntity>
    }

    companion object {

        private val TAG = "DataRepository"
    }
}
