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

package app.bolling.chucknorris.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import app.bolling.chucknorris.database.model.JokeEntity;
import io.reactivex.Flowable;

@Dao
public interface JokeDao {
    @Query("SELECT * FROM jokes where read = 0")
    Flowable<JokeEntity> getUnreadJokes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<JokeEntity> jokes);

    //return type long, means it will also return the id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(JokeEntity joke);

    @Query("select * from jokes where id = :jokeId")
    Flowable<JokeEntity> getJoke(String jokeId);

}
