package org.skyfaced.kpm_test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V : ViewBinding>(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    private var _binding: V? = null
    protected val binding get() = requireNotNull(_binding) { "Binding isn't initialized" }

    protected fun binding(block: V.() -> Unit) = binding.apply(block)

    protected abstract fun setupBinding(inflater: LayoutInflater, parent: ViewGroup?): V

    protected val TAG = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = setupBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}