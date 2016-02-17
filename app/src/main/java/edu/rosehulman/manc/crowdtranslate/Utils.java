package edu.rosehulman.manc.crowdtranslate;

import android.app.Fragment;
import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by manc on 2/17/2016.
 */
public class Utils {

    public static void makeToast(Fragment fragment, String text){
        Context parentActivity = fragment.getActivity();
        if (parentActivity != null){
            Toast.makeText(parentActivity, text, Toast.LENGTH_SHORT).show();
        }
    }
}
