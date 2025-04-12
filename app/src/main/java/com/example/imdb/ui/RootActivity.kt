package com.example.imdb.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.imdb.R
import com.example.imdb.navigation.Navigator
import com.example.imdb.navigation.NavigatorHolder
import com.example.imdb.navigation.NavigatorImpl
import com.example.imdb.ui.movies.MoviesFragment
import org.koin.android.ext.android.inject

class RootActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityRootBinding
    private val navigatorHolder: NavigatorHolder by inject()

    private lateinit var navigator: Navigator

//    private val navigator = NavigatorImpl(
//        fragmentContainerViewId = R.id.fragment_container,
//        fragmentManager = supportFragmentManager
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navigator = NavigatorImpl(
            fragmentContainerViewId = R.id.fragment_container,
            fragmentManager = supportFragmentManager
        )

        if (savedInstanceState == null)
            navigator.openFragment(MoviesFragment())

    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.attachNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.detachNavigator()
    }
}