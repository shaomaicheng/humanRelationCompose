package com.chenglei.humanrelationbooking.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.ComposeFragment
import com.chenglei.humanrelationbooking.books.BooksFragment
import com.chenglei.humanrelationbooking.compose.home.Hosts
import com.chenglei.humanrelationbooking.utils.ReflectionUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hangshun.huadian.android.common.arch.ActivityBase
import com.hangshun.huadian.android.common.arch.ToolbarConfig
import com.hangshun.huadian.android.common.utils.dp2px
import kotlinx.coroutines.InternalCoroutinesApi


class HomeActivity: ActivityBase() {

    private val viewPager:ViewPager2 by lazy {
        findViewById(R.id.pager)
    }

    private val homeTab: TabLayout by lazy {
        findViewById(R.id.homeTab)
    }

    private val fragments = listOf<Fragment>(
        BooksFragment(),
        ComposeFragment().apply { arguments = bundleOf("host" to "/relation") },
//        StatisticsFragment(),
//        MineFragment()
        ComposeFragment().apply { arguments = bundleOf("host" to Hosts.Mine) }
    )


    data class TabMeta(
        @StringRes val title:Int,
        @DrawableRes val light :Int,
        @DrawableRes val grey:Int
    )

    private val tabs by lazy {
        listOf(
            TabMeta(
                R.string.home_books,
                R.drawable.ic_books_light,
                R.drawable.ic_books_grey
            ),
            TabMeta(
                R.string.home_relation,
                R.drawable.ic_relation_light,
                R.drawable.ic_relation_grey
            ),
//            TabMeta(
//                R.string.home_statistics,
//                R.drawable.ic_statistics_light,
//                R.drawable.ic_statistics_grey
//            ),
            TabMeta(
                R.string.home_mine,
                R.drawable.ic_mine_light,
                R.drawable.ic_mine_grey
            )
        )
    }

    private val adapter : HomeAdapter by lazy {
        HomeAdapter(this, fragments)
    }

    override fun needToolBar() = false

    override fun toolBarConfig(): ToolbarConfig? {
        return null
    }

    override fun realContentView(): View {
        return LayoutInflater.from(this).inflate(
            R.layout.activity_home,
            this.getRoot(),
            false
        )
    }

    override fun initViews() {
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        viewPager.isUserInputEnabled = false

        tabs.forEach {
            homeTab.apply {
                val tab = newTab()
                addTab(tab)
                tab.apply {
                    this.setIcon(it.grey)
//                    this.text = getString(it.title)
                }
            }
        }
        homeTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                initAllTabGrey()
                tab?.icon?.setTint(ContextCompat.getColor(this@HomeActivity,R.color.primary_500))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        TabLayoutMediator(homeTab, viewPager) {tab,position->
//            tab.text = getString(tabs[position].title)
            tab.setIcon(tabs[position].light)
        }.attach()

        try {
            ReflectionUtils.setFieldValue(homeTab, "tabTextSize", dp2px(this, 11f).toFloat())
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    private fun initAllTabGrey() {
        for (i in 0 until homeTab.tabCount){
            homeTab.getTabAt(i)?.icon?.apply {
            val width = dp2px(this@HomeActivity,24f)
                setBounds(0,0,width,width)
            }?.setTint(
                ContextCompat.getColor(this,R.color.color_ccccccc)
            )
        }
    }
}

class HomeAdapter constructor(activity: HomeActivity, val fragments: List<Fragment>) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}