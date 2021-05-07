package com.holikov.myclient.main.adapter

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.holikov.myclient.R
import com.holikov.myclient.search.SearchFragment
import com.holikov.myclient.saved.SavedFragment

enum class NavigationPage(@IdRes val id: Int, val position: Int, val lazyPage: () -> Fragment) {

    SEARCH(R.id.actionSearch, 0, lazyPage = { SearchFragment() }),
    SAVED(R.id.actionSaved, 1, lazyPage = { SavedFragment() });

    companion object {

        fun from(id: Int) = values().first { it.id == id }

        fun position(id: Int) = from(id).position

        fun find(position: Int) = values().first { it.position == position }.id
    }
}