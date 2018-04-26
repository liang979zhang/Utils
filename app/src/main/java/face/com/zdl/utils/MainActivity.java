package face.com.zdl.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import face.com.zdl.cctools.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.e("aaa","aaa");
    }
}
