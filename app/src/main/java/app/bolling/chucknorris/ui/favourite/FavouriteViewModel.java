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

package app.bolling.chucknorris.ui.favourite;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import app.bolling.chucknorris.ChuckApp;
import app.bolling.chucknorris.DataRepository;
import app.bolling.chucknorris.R;
import app.bolling.chucknorris.ResourceUtil;
import app.bolling.chucknorris.SingleLiveEvent;
import app.bolling.chucknorris.database.model.JokeEntity;

public class FavouriteViewModel extends AndroidViewModel {

    @Inject
    DataRepository repository;
    @Inject
    ResourceUtil resources;


    //single live events
    private SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> loadingVisibilityEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> buttonVisibilityEvent = new SingleLiveEvent<>();

    private JokeEntity viewedJoke;
    private LiveData<List<JokeEntity>> liveData;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        ChuckApp.component.inject(this);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */

    public LiveData<List<JokeEntity>> getObservableJokes() {
        //LiveDataReactiveStreams will handle disposal of flowable
        if (liveData == null)
            liveData = LiveDataReactiveStreams.fromPublisher(repository.getJokes());
        return liveData;
    }

    public SingleLiveEvent<String> getObservableToast() {
        return toastEvent;
    }

    public SingleLiveEvent<Integer> getLoadingVisibilityEvent() {
        return loadingVisibilityEvent;
    }

    public SingleLiveEvent<Integer> getButtonVisibilityEvent() {
        return buttonVisibilityEvent;
    }

    public void onFavoriteClicked() {
        if (viewedJoke != null) {
            if (viewedJoke.isFavourite()) {
                toastEvent.setValue(resources.getString(R.string.removed_from_favourites));
                viewedJoke.setFavourite(false);
                repository.saveJoke(viewedJoke);
            } else {
                toastEvent.setValue(resources.getString(R.string.added_to_favourites));
                viewedJoke.setFavourite(true);
                repository.saveJoke(viewedJoke);
            }
        }
    }
}
