package app.khodko.catfood.core

import android.view.View
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG


abstract class BaseFragment : Fragment(), LifecycleObserver {

    private var errorSnackbar: Snackbar? = null
    private var infoSnackbar: Snackbar? = null

    @MainThread
    protected fun showInfoSnackbar(
        @StringRes resId: Int,
        duration: Int = LENGTH_LONG,
        maxLines: Int = 2
    ) {
        view?.let {
            infoSnackbar = Snackbar.make(it, resId, duration).apply {
                if (maxLines > 2) {
                    //todo: in future android updates check R.id.snackbar_text
                    val textView =
                        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    textView.maxLines = maxLines
                }
                show()
            }
        }
    }

    @MainThread
    protected fun showErrorSnackbar(
        @StringRes messageResId: Int,
        @StringRes actionResId: Int,
        listener: (v: View) -> Unit
    ) {
        view?.let {
            errorSnackbar = Snackbar.make(it, messageResId, LENGTH_INDEFINITE)
            errorSnackbar?.setAction(actionResId, View.OnClickListener(listener::invoke))?.show()
        }
    }

    protected fun dismissErrorSnackbar() {
        errorSnackbar?.apply { if (isShown) dismiss() }
    }

    protected fun dismissInfoSnackbar() {
        infoSnackbar?.apply { if (isShown) dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissErrorSnackbar()
        dismissInfoSnackbar()
    }

}