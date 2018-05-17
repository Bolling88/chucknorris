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

package app.bolling.chucknorris.database.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "jokes")
class JokeEntity {
    @SerializedName("icon_url")
    @Expose
    var iconUrl: String? = null
    @PrimaryKey
    @SerializedName("id")
    @Expose
    lateinit var id: String
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
    var isFavourite: Boolean = false
}
