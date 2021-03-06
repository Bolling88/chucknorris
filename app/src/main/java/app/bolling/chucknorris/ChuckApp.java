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

package app.bolling.chucknorris;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import app.bolling.chucknorris.dagger.AppComponent;
import app.bolling.chucknorris.dagger.ApplicationModule;
import app.bolling.chucknorris.dagger.DaggerAppComponent;
import app.bolling.chucknorris.dagger.RoomModule;
import io.fabric.sdk.android.Fabric;

/**
 * Android Application class. Used for accessing singletons.
 */
public class ChuckApp extends Application {

    public static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());

        component = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this)).roomModule(new RoomModule())
                .build();
    }
}
