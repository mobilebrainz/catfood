package app.khodko.catfood.core

import android.view.View
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import app.khodko.catfood.R
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        val bottomView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomView?.let { b ->
            view?.let { v ->
                infoSnackbar = Snackbar.make(v, resId, duration).apply {
                    anchorView = b
                    if (maxLines > 2) {
                        //todo: in future android updates check R.id.snackbar_text
                        val textView =
                            v.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        textView.maxLines = maxLines
                    }
                    show()
                }
            }
        }

    }

    @MainThread
    protected fun showErrorSnackbar(
        @StringRes messageResId: Int,
        @StringRes actionResId: Int,
        listener: (v: View) -> Unit
    ) {
        val bottomView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomView?.let { b ->
            view?.let { v ->
                errorSnackbar = Snackbar.make(v, messageResId, LENGTH_INDEFINITE)
                errorSnackbar!!.anchorView = b
                errorSnackbar?.setAction(actionResId, View.OnClickListener(listener::invoke))?.show()
            }
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