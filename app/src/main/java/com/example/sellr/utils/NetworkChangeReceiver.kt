import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.sellr.R
import com.example.sellr.noInternet

class NetworkChangeReceiver(private val fragmentManager: FragmentManager) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (isNetworkConnected(context)) {
            // Internet is available
            val navHostFragment = fragmentManager.findFragmentById(R.id.fragmentContainer)
            val currentFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
            if (currentFragment is noInternet) {
                if (currentFragment != null) {
                    currentFragment.findNavController().navigateUp()
                }
            }
        } else {
            // Internet is not available
            fragmentManager.beginTransaction()
                .replace(R.id.profileFragment, noInternet())
                .commit()
        }
    }

    private fun isNetworkConnected(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
