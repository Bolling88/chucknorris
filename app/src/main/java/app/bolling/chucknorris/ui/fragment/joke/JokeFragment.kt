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

package app.bolling.chucknorris.ui.fragment.joke

import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.bolling.chucknorris.ChuckApp
import app.bolling.chucknorris.util.DataRepository
import app.bolling.chucknorris.R
import app.bolling.chucknorris.util.ResourceUtil
import app.bolling.chucknorris.databinding.FragmentMainBinding
import javax.inject.Inject

class JokeFragment : Fragment() {
    private lateinit var mBinding: FragmentMainBinding

    @Inject
    lateinit var resources: ResourceUtil
    @Inject
    lateinit var repository: DataRepository
    @Inject
    lateinit var application: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChuckApp.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        // Create and set the adapter for the RecyclerView.
        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = resources.getString(R.string.app_name)

        val viewModel = ViewModelProviders.of(this, JokeViewModelFactory(resources, repository, application)).get(JokeViewModel::class.java)

        mBinding.button.setOnClickListener { viewModel.onNextJokeClicked() }
        mBinding.fab.setOnClickListener { viewModel.onFavoriteClicked() }

        //now we can hook up the observables to the view viewModel
        setUpObservables(viewModel)
    }

    private fun setUpObservables(model: JokeViewModel) {
        //LiveData observable
        model.jokeChangedEvent.observe(this, Observer { jokeEntity ->
            //handle UI updates
            mBinding.textJoke.text = jokeEntity?.value
            if (jokeEntity!!.isFavourite) {
                mBinding.fab.setImageResource(R.drawable.ic_favorite_white_24dp)
            } else {
                mBinding.fab.setImageResource(R.drawable.ic_favorite_border_white_24dp)
            }
        })

        if (model.jokeChangedEvent.value == null) {
            model.onNextJokeClicked()
        } else {
            mBinding.progress.visibility = View.GONE
            mBinding.button.visibility = View.VISIBLE
        }

        //observe toast events
        model.observableToast.observe(this, Observer { text ->
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        })

        //visibility events
        model.loadingVisibilityEvent.observe(this, Observer { visibility ->
            mBinding.progress.visibility = visibility!!
        })
        model.buttonVisibilityEvent.observe(this, Observer { visibility ->
            mBinding.button.visibility = visibility!!
        })
    }
}
