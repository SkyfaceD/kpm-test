package org.skyfaced.kpm_test.utils

import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun ViewBinding.snack(
    message: String,
    @BaseTransientBottomBar.Duration
    duration: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(root, message, duration).show()
}