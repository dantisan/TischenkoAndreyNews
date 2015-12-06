package com.andrewtis.tischenkoandreynews;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

/**
 * Created by andrew on 26.11.15.
 */
public class ActivityTest extends ActivityUnitTestCase<RssNewsActivity> {

    public ActivityTest(){
        super(RssNewsActivity.class);
    }
    public ActivityTest(Class<RssNewsActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the MainActivity of the target application
        //startActivity(new Intent(getInstrumentation().getTargetContext(), RssNewsActivity.class), null, null);

        // Getting a reference to the MainActivity of the target application
//        mainActivity = (MainActivity)getActivity();
//
//        // Getting a reference to the TextView of the MainActivity of the target application
//        TextView tvHello = (TextView) mainActivity.findViewById(in.wptrafficanalyzer.helloworld.R.id.hello_world);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTest(){
        RssNewsActivity hActivity = startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        getInstrumentation().callActivityOnStart(hActivity);
        //getInstrumentation().callActivityOnResume(hActivity);

       // GoodByeActivity gActivity = launchActivity("package.goodbye", GoodByeActivity.class, null);
        //gActivity.finish();
    }
}
