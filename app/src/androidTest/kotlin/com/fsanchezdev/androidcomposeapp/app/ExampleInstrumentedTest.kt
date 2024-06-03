package com.fsanchezdev.androidcomposeapp.app

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
internal class ExampleInstrumentedTest {

    @get:Rule
    internal val rule = createComposeRule()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.fsanchezdev.androidcomposeapp.app", appContext.packageName)
    }

    @Test
    fun appLaunchesAndLogInButtonIsVisible() {
        //TODO mirar
        launchActivity<MainActivity>()
        rule.onNodeWithContentDescription("Insert name")
            .performTextInput("Mario")
        rule.onNodeWithContentDescription("Greet button")
            .performClick()
        rule.onNodeWithContentDescription("Greeting")
            .assertTextEquals("Hello, Mario")
    }
}
