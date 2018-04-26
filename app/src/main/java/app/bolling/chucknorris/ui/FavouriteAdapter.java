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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import app.bolling.chucknorris.R;
import app.bolling.chucknorris.database.model.JokeEntity;
import app.bolling.chucknorris.databinding.JokeItemBinding;

public class FavouriteAdapter extends ListAdapter<JokeEntity, FavouriteAdapter.FavouriteViewHolder> {

    private List<JokeEntity> mProductList;

    @Nullable
    private final JokeClickCallback mJokeClickCallback;

    protected FavouriteAdapter(JokeClickCallback callback) {
        super(DIFF_CALLBACK);
        mJokeClickCallback = callback;
    }

    public static final DiffUtil.ItemCallback<JokeEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<JokeEntity>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull JokeEntity oldUser, @NonNull JokeEntity newUser) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldUser.getId() == newUser.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull JokeEntity oldUser, @NonNull JokeEntity newUser) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldUser.equals(newUser);
                }
            };

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        JokeItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.joke_item,
                        parent, false);
        binding.setCallback(mJokeClickCallback);
        return new FavouriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        holder.binding.setJoke(mProductList.get(position));
    }

    @Override
    public int getItemCount() {
        return mProductList == null ? 0 : mProductList.size();
    }

    static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        final JokeItemBinding binding;

        public FavouriteViewHolder(JokeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
