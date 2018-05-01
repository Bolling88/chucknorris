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

package app.bolling.chucknorris.ui.joke;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

import app.bolling.chucknorris.ChuckApp;
import app.bolling.chucknorris.DataRepository;
import app.bolling.chucknorris.R;
import app.bolling.chucknorris.ResourceUtil;
import app.bolling.chucknorris.SingleLiveEvent;
import app.bolling.chucknorris.database.model.JokeEntity;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class JokeViewModel extends AndroidViewModel {

    @Inject
    DataRepository repository;
    @Inject
    ResourceUtil resources;


    //single live events
    private SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> loadingVisibilityEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> buttonVisibilityEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<JokeEntity> jokeEvent = new SingleLiveEvent<>();

    private JokeEntity viewedJoke;

    public JokeViewModel(@NonNull Application application) {
        super(application);
        ChuckApp.component.inject(this);
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

    public SingleLiveEvent<JokeEntity> getJokeChangedEvent() {
        return jokeEvent;
    }


    public void onNextJokeClicked() {
        loadingVisibilityEvent.setValue(View.VISIBLE);
        buttonVisibilityEvent.setValue(View.GONE);
        repository.loadNewJoke()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jokeEntity -> {
                    viewedJoke = jokeEntity;
                    loadingVisibilityEvent.setValue(View.GONE);
                    buttonVisibilityEvent.setValue(View.VISIBLE);
                    jokeEvent.postValue(jokeEntity);
                });
    }

    public void onFavoriteClicked() {
        if (viewedJoke != null) {
            if (!viewedJoke.isFavourite()) {
                viewedJoke.setFavourite(true);
                repository.saveJoke(viewedJoke);
                jokeEvent.postValue(viewedJoke);
                toastEvent.setValue(resources.getString(R.string.added_to_favourites));
            } else {
                viewedJoke.setFavourite(false);
                repository.deleteJoke(viewedJoke);
                jokeEvent.postValue(viewedJoke);
                toastEvent.setValue(resources.getString(R.string.removed_from_favourites));
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
