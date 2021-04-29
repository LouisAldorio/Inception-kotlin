package com.example.inception.activity


import androidx.test.espresso.Espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.inception.R
import com.example.inception.adaptor.CommodityHolder
import com.example.inception.adaptor.ImageHolder
import com.example.inception.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RecycleViewToDetailTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LandingPage::class.java)

    @Before
    fun RegisterIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    fun RecycleViewToDetailPageTest(index : Int) {
        onView(withId(R.id.user_commodity_rv)).perform(actionOnItemAtPosition<CommodityHolder>(index, scrollTo()))
        onView(withId(R.id.user_commodity_rv)).perform(actionOnItemAtPosition<CommodityHolder>(index, click()))
        onView(withId(R.id.detail_activity)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(withId(R.id.image_carousel_rv)).perform(actionOnItemAtPosition<ImageHolder>(0, click()))

        Thread.sleep(500)
        pressBack()
        Thread.sleep(500)
    }

    @Test
    fun RunTest(){
        RecycleViewToDetailPageTest(5)
        RecycleViewToDetailPageTest(10)
        RecycleViewToDetailPageTest(3)
        RecycleViewToDetailPageTest(15)
        RecycleViewToDetailPageTest(20)
        RecycleViewToDetailPageTest(35)
        RecycleViewToDetailPageTest(34)
        RecycleViewToDetailPageTest(12)
    }

    @After
    fun UnRegisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}
