package com.aryanakbarpour.micromktinterviewtest

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aryanakbarpour.micromktinterviewtest.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread


@HiltAndroidTest
class TaskInstrumentedTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testButtonStartLabelSwitch() {

        hiltRule.inject()

        // Check label is initially start
        Espresso.onView(ViewMatchers.withId(R.id.start_btn))
            .check(
                ViewAssertions.matches(
                    Matchers.allOf(
                        ViewMatchers.isDisplayed(),
                        ViewMatchers.withText("Start")
                    )
                )
            )

        // Click on start button
        Espresso.onView(ViewMatchers.withId(R.id.start_btn))
            .perform(ViewActions.click())

        Thread.sleep(1000)
        // Check label switched to stop
        Espresso.onView(ViewMatchers.withId(R.id.start_btn))
            .check(
                ViewAssertions.matches(
                    Matchers.allOf(
                        ViewMatchers.isDisplayed(),
                        ViewMatchers.withText("Stop")
                    )
                )
            )
    }

    // Automated test for starting update timer to check if the ui updates. It's a little complicated. This needs a viewmodel injected by hilt.
//    @Test
//    fun testTimeUpdatesWithAPI(){
//        hiltRule.inject()
//
//        // Click on start button
//
//        Espresso.onView(ViewMatchers.withId(R.id.start_btn))
//            .perform(ViewActions.click())
//
//        ViewMatchers.withId(R.id.update_time_text).
//
//        val initialUpdateTimeText = Espresso.onView((ViewMatchers.withId(R.id.update_time_text))).
//
//    }
}