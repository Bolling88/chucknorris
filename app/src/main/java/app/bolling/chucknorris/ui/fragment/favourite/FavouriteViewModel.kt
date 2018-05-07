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

package app.bolling.chucknorris.ui.fragment.favourite

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import app.bolling.chucknorris.*
import app.bolling.chucknorris.database.model.JokeEntity
import javax.inject.Inject

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository: DataRepository
    @Inject
    lateinit var resources: ResourceUtil

    //single live events
    val observableToast = SingleLiveEvent<String>()

    var jokesLiveData: LiveData<List<JokeEntity>>

    init {
        ChuckApp.component.inject(this)
        jokesLiveData = LiveDataReactiveStreams.fromPublisher(repository.jokes)
    }


    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */

    //LiveDataReactiveStreams will handle disposal of flowable
    fun observableJokes() = jokesLiveData

    fun onFavoriteClicked(joke: JokeEntity?) {
        if (joke != null) {
            if (joke.isFavourite) {
                observableToast.value = resources.getString(R.string.removed_from_favourites)
                joke.isFavourite = false
                repository.saveJoke(joke)
            } else {
                observableToast.value = resources.getString(R.string.added_to_favourites)
                joke.isFavourite = true
                repository.saveJoke(joke)
            }
        }
    }

    fun onPause() {
        repository.deleteAllNonfavouriteJokes()
    }
}
