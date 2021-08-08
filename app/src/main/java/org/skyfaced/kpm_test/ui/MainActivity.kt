package org.skyfaced.kpm_test.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding isn't initialized" }

    private fun binding(block: ActivityMainBinding.() -> Unit) = binding.apply(block)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
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