package com.androidtechmanageapp

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.platform.app.InstrumentationRegistry
import com.androidtechmanageapp.testing.HiltTestActivity
import com.androidtechmanageapp.view.compose.NavigationTop
import com.androidtechmanageapp.viewmodel.TechViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ComposeTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var context: Context
    private lateinit var viewModel: TechViewModel

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        hiltRule.inject()
    }

    //Navigationによる画面遷移1
    @Test
    fun navigationToInsert() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            NavigationTop(viewModel)
        }

        //起動時
        composeTestRule.onNodeWithTag("TopScreen").assertExists()

        // Top から Insert への遷移
        composeTestRule.onNodeWithTag("FAB").performClick()
        composeTestRule.onNodeWithText(
            context.getString(R.string.btn_insert)
        ).assertExists()
    }

    //Navigationによる画面遷移2
    @Test
    fun navigationToUpdate() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            NavigationTop(viewModel)
        }

        //起動時
        composeTestRule.onNodeWithTag("TopScreen").assertExists()

        //Top から Update への遷移
        //"KMM"は既に登録済みのデータ
        composeTestRule.onNodeWithText("KMM").performClick()
        composeTestRule.onNodeWithText(
            context.getString(R.string.btn_update)
        ).assertExists()
    }
}