/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.bolling.chucknorris.ui.fragment.joke

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import app.bolling.chucknorris.*
import app.bolling.chucknorris.database.model.JokeEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class JokeViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
     lateinit var repository: DataRepository
    @Inject
     lateinit var resources: ResourceUtil

    //single live events
    val observableToast = SingleLiveEvent<String>()
    val loadingVisibilityEvent = SingleLiveEvent<Int>()
    val buttonVisibilityEvent = SingleLiveEvent<Int>()
    val jokeChangedEvent = MutableLiveData<JokeEntity>()

    private var viewedJoke: JokeEntity? = null

    init {
        ChuckApp.component.inject(this)
    }


    fun onNextJokeClicked() {
        loadingVisibilityEvent.value = View.VISIBLE
        buttonVisibilityEvent.value = View.GONE
        repository.loadNewJoke()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { jokeEntity ->
                    viewedJoke = jokeEntity
                    loadingVisibilityEvent.value = View.GONE
                    buttonVisibilityEvent.value = View.VISIBLE
                    jokeChangedEvent.postValue(jokeEntity)
                }
    }

    fun onFavoriteClicked() {
        if (viewedJoke != null) {
            if (!viewedJoke!!.isFavourite) {
                viewedJoke!!.isFavourite = true
                repository.saveJoke(viewedJoke)
                jokeChangedEvent.postValue(viewedJoke)
                observableToast.setValue(resources!!.getString(R.string.added_to_favourites))
            } else {
                viewedJoke!!.isFavourite = false
                repository.deleteJoke(viewedJoke)
                jokeChangedEvent.postValue(viewedJoke)
                observableToast.setValue(resources!!.getString(R.string.removed_from_favourites))
            }
        }
    }
}
