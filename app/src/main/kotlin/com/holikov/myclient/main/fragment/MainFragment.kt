package com.holikov.myclient.main.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.holikov.base.extensions.set
import com.holikov.myclient.R
import com.holikov.myclient.base.view.BaseFragment
import com.holikov.myclient.main.adapter.NavigationPage
import com.holikov.myclient.main.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.kodein.di.generic.instance
import ru.ldralighieri.corbind.material.itemReselections
import ru.ldralighieri.corbind.material.itemSelections
import ru.ldralighieri.corbind.viewpager.pageSelections
import java.util.concurrent.atomic.AtomicReference

internal class MainFragment : BaseFragment<StateMain, MainViewModel, MainNavigator>()  {


    private val currentPage = AtomicReference(0)
    override val layoutResourceId = R.layout.main_fragment
    override val fragmentModule = MainModule()

    override val viewModel by instance<MainViewModel>()
    override val navigator by instance<MainNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    override fun onStateChanged(state: StateMain) {}

    private fun setupNavigation() {

        bottomNavigation.pageSelect(this::pageClicked).bind()
        bottomNavigation.pageReselect(this::pageClicked).bind()
        viewPager.apply {
            set(ViewPagerAdapter(childFragmentManager).also { offscreenPageLimit = it.count })
            pageSelections().onEach { pageSelected(it) }.bind()
        }
    }

    private fun pageClicked(position: Int) {
        if (position.isCurrent().not()) viewPager.select(position)
        currentPage.set(position)
    }

    private fun pageSelected(position: Int) {
        bottomNavigation.check(position)
    }

    private fun Int.isCurrent() = this == currentPage.get()

    private fun BottomNavigationView.pageSelect(action: (Int) -> Unit) = itemSelections().menuAction(action)

    private fun BottomNavigationView.pageReselect(action: (Int) -> Unit) = itemReselections().menuAction(action)

    private fun Flow<MenuItem>.menuAction(action: (Int) -> Unit) =
        onEach { action(NavigationPage.position(it.itemId)) }

    private fun BottomNavigationView.check(position: Int) {
        selectedItemId = NavigationPage.find(position)
    }

    private fun ViewPager.select(page: NavigationPage) = select(page.position)

    private fun ViewPager.select(position: Int) = setCurrentItem(position, false)
}