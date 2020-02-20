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
import com.google.samples.apps.sunflower.model.GardenList
import com.moquality.android.RoboConfig
import com.moquality.android.RoboState
import com.moquality.android.RoboTest
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Method

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
    fun robo() {
        val state = RoboTest(Config).run(GardenList.get(), 3)

        fun RoboState?.forEach(f: (state: RoboState) -> Unit) {
            if (this == null) {
                return
            }

            f(this)
            return this.previous.forEach(f)
        }

        state.forEach {
            val error = it.error
            if (error != null) {
                System.err.println("${it.previous?.currentPage?.simpleName}.${it.method}(): ${error.message}")
            }
        }
    }
}

object Config : RoboConfig {
    override fun generateArguments(state: RoboState, method: Method) = when (method.name) {
        "openDescription" -> listOf(arrayOf("Avocado", "Tomato", "Apple").random())
        else -> super.generateArguments(state, method)
    }
}