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

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import app.bolling.chucknorris.ChuckApp;
import app.bolling.chucknorris.R;
import app.bolling.chucknorris.ResourceUtil;
import app.bolling.chucknorris.database.model.JokeEntity;
import app.bolling.chucknorris.databinding.FragmentFavouriteBinding;

public class FavouritesFragment extends Fragment implements JokeAdapterCallbacks {

    public static final String KEY_JOKE_ID = "product_id";
    private FragmentFavouriteBinding mBinding;

    @Inject
    ResourceUtil resources;

    private FavouriteAdapter adapter;
    private FavouriteViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChuckApp.component.inject(this);
        adapter = new FavouriteAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false);

        // Create and set the adapter for the RecyclerView.
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.recylerView.setAdapter(adapter);
        mBinding.recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel =
                ViewModelProviders.of(this).get(FavouriteViewModel.class);

        //now we can hook up the observables to the view viewModel
        setUpObservables(viewModel);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewModel != null)
            viewModel.onPause();
    }

    private void setUpObservables(FavouriteViewModel model) {
        //LiveData observable
        model.getObservableJokes().observe(this, jokes -> {
            if (jokes != null && jokes.size() > 0) {
                adapter.setJokes(jokes);
            }
        });

        //observe toast events
        model.getObservableToast().observe(this, text -> Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onFavClicked(JokeEntity joke) {
        if (viewModel != null)
            viewModel.onFavoriteClicked(joke);
    }
}
