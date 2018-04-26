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

package app.bolling.chucknorris.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
import io.reactivex.disposables.Disposable;

public class JokeViewModel extends AndroidViewModel {

    @Inject
    DataRepository repository;
    @Inject
    ResourceUtil resources;


    //single live events
    private SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> loadingVisibilityEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> buttonVisibilityEvent = new SingleLiveEvent<>();

    private JokeEntity viewedJoke;
    private MutableLiveData<JokeEntity> liveData = new MutableLiveData<>();
    private Disposable jokeDisposable;

    public JokeViewModel(@NonNull Application application) {
        super(application);
        ChuckApp.component.inject(this);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */

    public LiveData<JokeEntity> getObservableJoke() {


        //liveData = LiveDataReactiveStreams.fromPublisher(flowable);

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


    public void onNextJokeClicked() {
        loadingVisibilityEvent.setValue(View.VISIBLE);
        buttonVisibilityEvent.setValue(View.GONE);
        repository.loadNewJoke()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jokeEntity -> {
                    viewedJoke = jokeEntity;
                    loadingVisibilityEvent.setValue(View.GONE);
                    buttonVisibilityEvent.setValue(View.VISIBLE);
                    liveData.postValue(jokeEntity);

                    //also observe changes to this entity
                    disposeJokeDisposable();
                    jokeDisposable = repository.getJoke(jokeEntity.getId())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(entity -> liveData.postValue(entity));
                });
    }

    public void onJokeRead() {
        if (viewedJoke != null) {
            viewedJoke.setRead(true);
            repository.saveJoke(viewedJoke);
        }
    }

    public void onFavoriteClicked() {
        if (viewedJoke != null) {
            if (viewedJoke.isFavourite()) {
                toastEvent.setValue(resources.getString(R.string.removed_from_favourites));
                viewedJoke.setFavourite(false);
                repository.saveJoke(viewedJoke);
            } else {
                toastEvent.setValue(resources.getString(R.string.removed_added_to_favourites));
                viewedJoke.setFavourite(true);
                repository.saveJoke(viewedJoke);
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //clear all observable connections to the db
        disposeJokeDisposable();
    }

    private void disposeJokeDisposable() {
        if(jokeDisposable != null && !jokeDisposable.isDisposed()){
            jokeDisposable.dispose();
        }
    }
}
