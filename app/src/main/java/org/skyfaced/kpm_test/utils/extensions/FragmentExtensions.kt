package org.skyfaced.kpm_test.utils.extensions

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.skyfaced.kpm_test.ui.MainActivity

fun Fragment.activitySnack(
    message: String,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT,
) {
    (requireActivity() as MainActivity).snack(message, duration)
}