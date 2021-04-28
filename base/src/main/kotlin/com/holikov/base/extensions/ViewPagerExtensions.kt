package com.holikov.base.extensions

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

fun ViewPager.set(pagerAdapter: PagerAdapter) = adapter ?: run { adapter = pagerAdapter }

fun ViewPager.hasAdapter() = adapter != null

