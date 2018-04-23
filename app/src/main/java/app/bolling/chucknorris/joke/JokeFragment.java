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

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.bolling.chucknorris.R;
import app.bolling.chucknorris.databinding.FragmentMainBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class JokeFragment extends Fragment {

    public static final String KEY_JOKE_ID = "product_id";
    private FragmentMainBinding mBinding;

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

        //create factory, used to inject dependencies to the view model
        JokeViewModel.Factory factory = new JokeViewModel.Factory(
                getActivity().getApplication(), getArguments());

        //create or reference the view model with the above factory
        final JokeViewModel model = ViewModelProviders.of(this, factory)
                .get(JokeViewModel.class);

        //now we can hook up the observables to the view model
        setUpObservables(model);
    }

    private void setUpObservables(JokeViewModel model) {
        //Observe joke
        model.getObservableJoke().observeOn(AndroidSchedulers.mainThread()).subscribe(comicEntity -> {
            //let the view model also get the latest product
            model.onJokeUpdated(comicEntity);
            //handle UI updates
            mBinding.textJoke.setText(comicEntity.getValue());
        });

        //observe toast events
        model.getObservableToast().observe(this, text -> {
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        });

        //observe loading visibility events
        model.getObservableLoadingVisibility().observe(this, visibility -> {
            mBinding.frameLoading.setVisibility(visibility);
        });
    }

    /** Creates product fragment for specific product ID */
    public static JokeFragment forProduct(int productId) {
        JokeFragment fragment = new JokeFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_JOKE_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }
}
