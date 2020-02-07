/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.model

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.samples.apps.sunflower.R
import org.hamcrest.Matchers.allOf

class GardenList {
    companion object {
        private val instance = GardenList()
        fun get() = instance
    }

    fun openDescription(name: String): PlantDescription<GardenList> {
        onView(withText(name)).perform(click())
        return PlantDescription.from(this)
    }

    fun openPlantList(useAddButton: Boolean): PlantList {
        val matcher = if (useAddButton) {
            withId(R.id.add_plant)
        } else {
            withContentDescription("Plant list")
        }

        onView(matcher).perform(click())
        return PlantList.get()
    }
}

class PlantList {
    companion object {
        private val instance = PlantList()
        fun get() = instance
    }

    fun openDescription(name: String): PlantDescription<PlantList> {
        onView(withText(name)).perform(click())
        return PlantDescription.from(this)
    }

    fun openGardenList(): GardenList {
        onView(withContentDescription("My garden")).perform(click())
        return GardenList.get()
    }
}

class PlantDescription<T>(private val parent: T) {
    companion object {
        fun <T> from(parent: T) = PlantDescription(parent)
    }

    fun add(): PlantDescription<T> {
        onView(withId(R.id.fab))
                .withFailureHandler { error, _ ->
                    if (error !is PerformException) {
                        throw error
                    }

                    error.printStackTrace()
                }
                .perform(click())
        return this
    }

    fun backToList(): T {
        onView(allOf(withParent(withId(R.id.toolbar)), withParentIndex(0))).perform(click())
        return parent
    }
}