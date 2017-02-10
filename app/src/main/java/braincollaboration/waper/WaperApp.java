package braincollaboration.waper;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class WaperApp extends Application {

    private static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(createActivityStateHandler());
    }

    private ActivityLifecycleCallbacks createActivityStateHandler() {
        return new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }

    private void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

}
