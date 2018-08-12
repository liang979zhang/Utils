package face.com.zdl.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private LinearLayout ll_ll, ll_ll2, ll_ll3, ll_ll4;
    private RelativeLayout rl, rl2;

    private ViewPager viewPager;


    private List<View> viewlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ll_ll = findViewById(R.id.ll_ll);
        ll_ll2 = findViewById(R.id.ll_ll2);
        ll_ll3 = findViewById(R.id.ll_ll3);
        ll_ll4 = findViewById(R.id.ll_ll4);
        rl = findViewById(R.id.rl);
        rl2 = findViewById(R.id.rl2);
//        rl.setRotationX(70);

        View view = LayoutInflater.from(this).inflate(R.layout.view1, null,false);
        View view2 = LayoutInflater.from(this).inflate(R.layout.view2, null,false);
        View view3 = LayoutInflater.from(this).inflate(R.layout.view3, null,false);
        View view4 = LayoutInflater.from(this).inflate(R.layout.view4, null,false);
        View view5 = LayoutInflater.from(this).inflate(R.layout.view4, null,false);
        View view6 = LayoutInflater.from(this).inflate(R.layout.view4, null,false);

        viewlist.add(view);
        viewlist.add(view2);
        viewlist.add(view3);
        viewlist.add(view4);
        viewlist.add(view5);
        viewlist.add(view6);
        ViewapgerAdapter viewapgerAdapter = new ViewapgerAdapter(viewlist);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setPageMargin(40);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewapgerAdapter);

        rl.setRotationY(50);
        rl2.setRotationY(-50);


    }

    public void onclik(View view) {
//        ActivityAlertDialogManager.displayOneBtnDialog(this, "是", "aa", "确定", "取消", -1, new DialogClick() {
//            @Override
//            public void CancelClick(DialogInterface dialog, int which) {
//                ToastUtils.showShortToast(MainActivity.this, "aaa");
//            }
//
//            @Override
//            public void ComfirmClick(DialogInterface dialog, int which) {
//
//            }
//        }).show();


//        viewset();


//        startActivity(new Intent(this, SecondActivity.class));

    }



    private void viewset() {


        ll_ll.post(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation(0, -ll_ll.getWidth(), 0.0f, 0);
                animation.setDuration(3000);
                ll_ll.startAnimation(animation);
            }
        });


        ll_ll2.post(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation(ll_ll2.getWidth(), 0, 0.0f, 0);
                animation.setDuration(3000);
                ll_ll2.startAnimation(animation);
            }
        });


        ll_ll3.post(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation(0, ll_ll3.getWidth(), 0.0f, 0);
                animation.setDuration(3000);
                ll_ll3.startAnimation(animation);
            }
        });


        ll_ll4.post(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation(-ll_ll4.getWidth(), 0, 0.0f, 0);
                animation.setDuration(3000);
                ll_ll4.startAnimation(animation);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300) {
            Log.e("tag", "data==" + data);
        }
    }
}
