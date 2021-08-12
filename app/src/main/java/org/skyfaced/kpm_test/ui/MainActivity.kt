package org.skyfaced.kpm_test.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.ActivityMainBinding
import org.skyfaced.kpm_test.utils.GlobalNavigationHandler
import org.skyfaced.kpm_test.utils.GlobalNavigator
import org.skyfaced.kpm_test.utils.NetworkState
import org.skyfaced.kpm_test.utils.extensions.clearCredentials

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), GlobalNavigationHandler {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding isn't initialized" }
    private fun binding(block: ActivityMainBinding.() -> Unit) = binding.apply(block)

    private val networkState by inject<NetworkState>()
    private val preferences by inject<SharedPreferences>()

    private var _navController: NavController? = null
    private val navController get() = requireNotNull(_navController) { "Nav controller isn't initialized" }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_KPM)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNetwork()
        setupNavigation()
    }

    private fun setupNetwork() {
        lifecycleScope.launch {
            networkState.flow.flowWithLifecycle(lifecycle).collect { isAvailable ->
                binding.networkView.isVisible = !isAvailable
            }
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        _navController = navHostFragment.navController

        binding {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                val isNavViewVisible = when (destination.id) {
                    R.id.start_fragment, R.id.authentication_fragment -> false
                    else -> true
                }
                navView.isVisible = isNavViewVisible
            }

            navView.setupWithNavController(navController)
        }
    }

    fun snack(
        message: String,
        @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT,
        anchorView: View = binding.navView,
    ) {
        Snackbar.make(binding.root, message, duration)
            .setAnchorView(anchorView)
            .show()
    }

    override fun onStart() {
        super.onStart()
        GlobalNavigator.registerHandler(this)
    }

    override fun onStop() {
        GlobalNavigator.unregisterHandler()
        super.onStop()
    }

    override fun logout(why: String) {
        runOnUiThread {
            snack(why, anchorView = binding.root)
            preferences.clearCredentials()
            //TODO fix navigation
            navController.setGraph(R.navigation.graph_start)
        }
    }
}