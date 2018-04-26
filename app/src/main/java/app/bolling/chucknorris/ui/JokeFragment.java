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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import app.bolling.chucknorris.ChuckApp;
import app.bolling.chucknorris.R;
import app.bolling.chucknorris.ResourceUtil;
import app.bolling.chucknorris.databinding.FragmentMainBinding;

public class JokeFragment extends Fragment {

    public static final String KEY_JOKE_ID = "product_id";
    private FragmentMainBinding mBinding;

    @Inject
    ResourceUtil resources;
    @Inject JokeViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChuckApp.component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        // Create and set the adapter for the RecyclerView.
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBinding.button.setOnClickListener(v -> model.onNextJokeClicked());
        mBinding.fab.setOnClickListener(view -> model.onFavoriteClicked());

        //now we can hook up the observables to the view model
        setUpObservables(model);
    }

    private void setUpObservables(JokeViewModel model) {
        //LiveData observable
        model.getObservableJoke().observe(this, jokeEntity -> {
            //handle UI updates
            mBinding.textJoke.setText(jokeEntity.getValue());
            model.onJokeRead();
        });

        //observe toast events
        model.getObservableToast().observe(this, text -> Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show());

        //visibility events
        model.getLoadingVisibilityEvent().observe(this, visibility -> mBinding.progress.setVisibility(visibility));
        model.getButtonVisibilityEvent().observe(this, visibility -> mBinding.button.setVisibility(visibility));
    }
}
