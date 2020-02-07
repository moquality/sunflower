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
                .openDescription("Avocado")
                .add()
                .backToList()
                .openGardenList()
    }

    @Test
    fun generate() {
        val model = genModels(GardenList::class.java)
        val gson = GsonBuilder().run {
            setPrettyPrinting()
        }.create()
        gson.toJson(model)
    }

    @Test
    fun robo() {
        val config = RoboConfig("""{
          "com.google.samples.apps.sunflower.model.PlantDescription": {
            "methods": {
              "add": {
                "params": [],
                "returns": "com.google.samples.apps.sunflower.model.PlantDescription",
                "weight": 1
              },
              "backToList": {
                "params": [],
                "returns": "generic",
                "weight": 1
              }
            }
          },
          "com.google.samples.apps.sunflower.model.PlantList": {
            "methods": {
              "openDescription": {
                "params": [
                  {
                    "type": "java.lang.String",
                    "valid": ["Apple", "Avocado", "Eggplant"]
                  }
                ],
                "returns": "com.google.samples.apps.sunflower.model.PlantDescription",
                "weight": 1
              },
              "openGardenList": {
                "params": [],
                "returns": "com.google.samples.apps.sunflower.model.GardenList",
                "weight": 1
              }
            }
          },
          "com.google.samples.apps.sunflower.model.GardenList": {
            "methods": {
              "openDescription": {
                "params": [
                  {
                    "type": "java.lang.String",
                    "valid": ["Apple", "Avocado", "Eggplant"]
                  }
                ],
                "returns": "com.google.samples.apps.sunflower.model.PlantDescription",
                "weight": 1
              },
              "openPlantList": {
                "params": [
                  {
                    "type": "boolean"
                  }
                ],
                "returns": "com.google.samples.apps.sunflower.model.PlantList",
                "weight": 1
              }
            }
          }
        }""")

        RoboTest(config).run(GardenList.get())
    }
}