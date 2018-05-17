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

package app.bolling.chucknorris.ui.fragment.favourite

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import app.bolling.chucknorris.ChuckApp
import app.bolling.chucknorris.R
import app.bolling.chucknorris.util.ResourceUtil
import app.bolling.chucknorris.database.model.JokeEntity
import app.bolling.chucknorris.databinding.FragmentFavouriteBinding
import javax.inject.Inject

class FavouritesFragment : Fragment(), FavouriteAdapterCallbacks {
    private lateinit var mBinding: FragmentFavouriteBinding

    @Inject
    lateinit var resources: ResourceUtil

    private lateinit var adapter: FavouriteAdapter
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        ChuckApp.component.inject(this)
        adapter = FavouriteAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false)

        // Create and set the adapter for the RecyclerView.
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recylerView.adapter = adapter
        mBinding.recylerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = resources.getString(R.string.favourites)
        viewModel = ViewModelProviders.of(this).get(FavouriteViewModel::class.java)

        //now we can hook up the observables to the view viewModel
        setUpObservables(viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun setUpObservables(model: FavouriteViewModel) {
        //LiveData observable
        model.observableJokes().observe(this, Observer { jokes ->
            adapter.setJokes(jokes!!)
            if (jokes.isEmpty()) {
                mBinding.imageBackground.visibility = View.VISIBLE
                mBinding.textJoke.visibility = View.VISIBLE
            } else {
                mBinding.imageBackground.visibility = View.GONE
                mBinding.textJoke.visibility = View.GONE
            }
        })


        //observe toast events
        model.observableToast.observe(this, Observer { text ->
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onFavClicked(joke: JokeEntity) {
        viewModel.onFavoriteClicked(joke)
    }
}

