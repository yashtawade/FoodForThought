package com.yashtawade.foodforthought;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by yashtawade on 10/5/16.
 */

public class Menu extends Activity {
    String username = getIntent().getStringExtra("Username");
    TextView textView = (TextView)findViewById(R.id.menuUsername);


}
