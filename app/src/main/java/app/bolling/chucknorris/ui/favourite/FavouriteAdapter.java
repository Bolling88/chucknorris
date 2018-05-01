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

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import app.bolling.chucknorris.R;
import app.bolling.chucknorris.database.model.JokeEntity;
import app.bolling.chucknorris.databinding.JokeItemBinding;
import app.bolling.chucknorris.ui.JokeClickCallback;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    List<JokeEntity> jokeList;

    @Nullable
    private final JokeClickCallback mJokeClickCallback;

    protected FavouriteAdapter(JokeClickCallback callback) {
        mJokeClickCallback = callback;
    }

    public void setJokes(final List<JokeEntity> newJokesList) {
        if (jokeList == null) {
            jokeList = newJokesList;
            notifyItemRangeInserted(0, newJokesList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return jokeList.size();
                }

                @Override
                public int getNewListSize() {
                    return jokeList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return jokeList.get(oldItemPosition).getId().equals(jokeList.get(newItemPosition).getId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return jokeList.get(oldItemPosition).getValue().equals(jokeList.get(newItemPosition).getValue());
                }
            });
            jokeList = newJokesList;
            result.dispatchUpdatesTo(this);
        }
    }

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
        holder.binding.setJoke(jokeList.get(position));
    }

    @Override
    public int getItemCount() {
        if(jokeList == null)
            return 0;
        return jokeList.size();
    }

    static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        final JokeItemBinding binding;

        public FavouriteViewHolder(JokeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
