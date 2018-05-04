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

package app.bolling.chucknorris.ui.favourite

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import app.bolling.chucknorris.R
import app.bolling.chucknorris.database.model.JokeEntity
import app.bolling.chucknorris.databinding.JokeItemBinding

class FavouriteAdapter(private val mJokeClickCallback: JokeAdapterCallbacks?) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    internal var jokeList: List<JokeEntity>? = null

    fun setJokes(newJokesList: List<JokeEntity>) {
        if (jokeList == null) {
            jokeList = newJokesList
            notifyItemRangeInserted(0, newJokesList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return jokeList!!.size
                }

                override fun getNewListSize(): Int {
                    return jokeList!!.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return jokeList!![oldItemPosition].id == jokeList!![newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return jokeList!![oldItemPosition].value == jokeList!![newItemPosition].value
                }
            })
            jokeList = newJokesList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val binding = DataBindingUtil
                .inflate<JokeItemBinding>(LayoutInflater.from(parent.context), R.layout.joke_item,
                        parent, false)
        binding.callback = mJokeClickCallback
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.binding.joke = jokeList!![position]
    }

    override fun getItemCount(): Int {
        return if (jokeList == null) 0 else jokeList!!.size
    }

    class FavouriteViewHolder(val binding: JokeItemBinding) : RecyclerView.ViewHolder(binding.root)
}
