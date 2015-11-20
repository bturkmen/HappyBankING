package ingbank.com.tr.happybanking;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by burcuturkmen on 20/11/15.
 */
public class HappyBankingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "QTwKDdMAPUAZA9TMgWtvbDO0NnqkC8VvVwzYbjSF", "TwiY9q9Oe7vWewkbPUv2KVgNAwPuQD6EAMOiWZSe");
        // Specify an Activity to handle all pushes by default.
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
