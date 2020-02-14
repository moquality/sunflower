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

package com.google.samples.apps.sunflower

import androidx.test.rule.ActivityTestRule
import com.google.gson.GsonBuilder
import com.google.samples.apps.sunflower.model.GardenList
import com.google.samples.apps.sunflower.model.PlantList
import com.moquality.android.RoboConfig
import com.moquality.android.RoboTest
import com.moquality.android.genModels
import org.junit.Rule
import org.junit.Test

class Robo {
    @get:Rule
    val activityRule = ActivityTestRule(GardenActivity::class.java)

    @Test
    fun model() {
        GardenList.get()
                .openPlantList(false)
                .expectPlantList()
                .openDescription("Avocado")
                .add()
                .backToList()
                .openGardenList()
                .expectGardenList()
    }

    @Test
    fun generate() {
        val model = genModels(GardenList::class.java)
        val gson = GsonBuilder().run {
            setPrettyPrinting()
        }.create()
        gson.toJson(model)
    }

    @ExperimentalStdlibApi
    @Test
    fun robo() {
        val file = javaClass.classLoader?.getResourceAsStream("assets/roboconfig.json")
                ?: error("Unable to find robo config file")
        val config = RoboConfig(file.readBytes().decodeToString())

        RoboTest(config).run(GardenList.get())
    }
}