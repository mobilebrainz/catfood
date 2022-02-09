package app.khodko.catfood.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.internal.OnConnectionFailedListener


abstract class BaseSignInActivity : AppCompatActivity(), OnConnectionFailedListener {

    protected var account: GoogleSignInAccount? = null

    fun getGoogleSinginClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            //.requestProfile()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    fun isUserSignedIn(): Boolean {
        account = GoogleSignIn.getLastSignedInAccount(this)
        return account != null
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

}