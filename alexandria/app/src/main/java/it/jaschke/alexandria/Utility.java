package it.jaschke.alexandria;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by graingersoftware on 10/26/15.
 */
public class Utility
{
    /**
     *
     * @param c Context of the calling activity
     * @return boolean reflecting status of network
     */
    public static boolean isNetworkAvailable(Context c)
    {

        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
