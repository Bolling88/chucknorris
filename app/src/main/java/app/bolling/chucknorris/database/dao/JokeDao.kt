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

package app.bolling.chucknorris.database.dao

import android.arch.persistence.room.*
import app.bolling.chucknorris.database.model.JokeEntity
import io.reactivex.Flowable

@Dao
interface JokeDao {

    @Query("select * from jokes")
    fun getJokes(): Flowable<List<JokeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(jokes: List<JokeEntity>)

    //return type long, means it will also return the id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(joke: JokeEntity): Long

    @Query("select * from jokes where id = :jokeId")
    fun getJoke(jokeId: String): Flowable<JokeEntity>

    @Delete
    fun deleteJoke(entity: JokeEntity)

    @Query("DELETE FROM jokes WHERE isFavourite = 0")
    fun deleteAllNonfavouriteJokes()
}
