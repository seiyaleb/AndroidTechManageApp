package com.androidtechmanageapp

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.platform.app.InstrumentationRegistry
import com.androidtechmanageapp.model.Tech
import com.androidtechmanageapp.model.TechAndURL
import com.androidtechmanageapp.model.URL
import com.androidtechmanageapp.testing.HiltTestActivity
import com.androidtechmanageapp.view.compose.DetailToolBar
import com.androidtechmanageapp.view.compose.NavigationTop
import com.androidtechmanageapp.view.compose.TechItem
import com.androidtechmanageapp.viewmodel.TechViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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

    //各アイテムの内容
    @Test
    fun techItemIsDisplayed() {

        //テストデータ作成
        val tech = Tech(title = "Compose Multiplatform", detail = "", category = "")
        val url = URL(tech_id = 1, url = "")
        val techAndUrl = TechAndURL().apply {
            this.tech = tech
            this.urls = listOf(url)
        }

        composeTestRule.setContent {
            viewModel = hiltViewModel()
            TechItem(techAndUrl, viewModel) {}
        }

        //指定したタグが表示されているか確認
        composeTestRule.onNodeWithTag("item").assertIsDisplayed()

        //各アイテム内のテキストと、テストデータが一致していることを確認
        composeTestRule.onNodeWithText(techAndUrl.tech.title).assertIsDisplayed()
    }

    //削除確認ダイアログ
    @Test
    fun displaysDeleteDialog() {
        val openDialog = mutableStateOf(false)

        composeTestRule.setContent {
            DetailToolBar(openDialog)
        }

        assertFalse(openDialog.value)

        //削除アイコンをタップ
        composeTestRule.onNodeWithTag("delete").performClick()

        //openDialogがtrueになった（＝削除確認ダイアログが表示された）ことを確認
        assertTrue(openDialog.value)
    }
}