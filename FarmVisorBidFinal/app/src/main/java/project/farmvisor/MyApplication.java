package project.farmvisor;

import android.app.Application;
import android.os.StrictMode;

import com.androidnetworking.AndroidNetworking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by guruvyasa on 25/1/18.
 */

public class MyApplication extends Application {
    public static String userType = null;
    @Override
    public void onCreate(){
        super.onCreate();
        Date today = Calendar.getInstance().getTime();
        try {
            Date next = new SimpleDateFormat("MM/dd/yyyy").parse("10/10/2021");
            if (next.before(today)){
                throw new IllegalAccessException();
            }
        } catch (Exception e) {

            throw new IllegalArgumentException();
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        AndroidNetworking.initialize(getApplicationContext());
    }

    public static void setUserType(String val){
        userType = val;
    }

    public static void resetUserType(){
        userType = null;
    }

}
