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

package app.bolling.chucknorris.joke;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import app.bolling.chucknorris.BasicApp;
import app.bolling.chucknorris.DataRepository;
import app.bolling.chucknorris.SingleLiveEvent;
import app.bolling.chucknorris.database.model.JokeEntity;
import io.reactivex.Observable;

public class JokeViewModel extends AndroidViewModel {

    private final DataRepository repository;


    //single live events
    private SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> loadingVisibilityEvent = new SingleLiveEvent<>();

    private final Bundle arguments;

    public JokeViewModel(@NonNull Application application, DataRepository repository,
                         final Bundle arguments) {
        super(application);
        this.repository = repository;
        this.arguments = arguments;
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */

    public Observable<JokeEntity> getObservableJoke() {
        return repository.getJoke(arguments.getInt(JokeFragment.KEY_JOKE_ID));
    }

    public void onJokeUpdated(JokeEntity joke) {
        if(joke == null){
            toastEvent.setValue("Joke was null");
        }else {
            toastEvent.setValue("Joke retrieved");
        }
        loadingVisibilityEvent.setValue(View.GONE);
    }

    public SingleLiveEvent<String> getObservableToast(){
        return toastEvent;
    }

    public SingleLiveEvent<Integer> getObservableLoadingVisibility(){
        return loadingVisibilityEvent;
    }

    /**
     * A creator is used to inject dependencies into the view model
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Bundle mBundle;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, Bundle bundle) {
            mApplication = application;
            mBundle = bundle;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new JokeViewModel(mApplication, mRepository, mBundle);
        }
    }
}
