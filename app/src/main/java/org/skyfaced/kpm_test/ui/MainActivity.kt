package org.skyfaced.kpm_test.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.ActivityMainBinding
import org.skyfaced.kpm_test.utils.NetworkState

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding isn't initialized" }
    private fun binding(block: ActivityMainBinding.() -> Unit) = binding.apply(block)

    private val networkState by inject<NetworkState>()

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
        val navController = navHostFragment.navController

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
}