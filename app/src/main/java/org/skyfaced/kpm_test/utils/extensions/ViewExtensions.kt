package org.skyfaced.kpm_test.utils.extensions

import android.view.View
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun ViewBinding.snack(
    message: String,
    @BaseTransientBottomBar.Duration
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar.make(root, message, duration).show()
}

fun View.snack(
    message: String,
    @BaseTransientBottomBar.Duration
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar.make(this, message, duration).show()
}