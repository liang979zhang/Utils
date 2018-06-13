package face.com.zdl.utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;




public class MainActivity extends AppCompatActivity {


    private LinearLayout ll_ll,ll_ll2;
    private RelativeLayout rl,rl2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_ll = findViewById(R.id.ll_ll);
        ll_ll2 = findViewById(R.id.ll_ll2);
        rl = findViewById(R.id.rl);
        rl2 = findViewById(R.id.rl2);
//        rl.setRotationX(70);








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


        rl.setRotationY(50);
        rl2.setRotationY(-50);


        ll_ll.post(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation( 0,-ll_ll.getWidth(), 0.0f, 0);
                animation.setDuration(3000);
                ll_ll.startAnimation(animation);
            }
        });


        ll_ll2.post(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation( ll_ll2.getWidth(),0, 0.0f, 0);
                animation.setDuration(3000);
                ll_ll2.startAnimation(animation);
            }
        });

    }


}
