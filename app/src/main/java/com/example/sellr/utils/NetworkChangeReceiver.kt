import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.sellr.MainActivity
import com.example.sellr.MainFragmentHolder
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
            val i = Intent(context, MainFragmentHolder::class.java)
            i.putExtra("nointernet", "nointernet")
            if (context != null) {
                context.startActivity(i)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        if (network != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }
        return false
    }
}
