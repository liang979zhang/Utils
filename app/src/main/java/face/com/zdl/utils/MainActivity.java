package face.com.zdl.utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import face.com.zdl.cctools.ToastUtils;
import face.com.zdl.cctools.dialog.ActivityAlertDialogManager;
import face.com.zdl.cctools.dialog.DialogClick;


public class MainActivity extends AppCompatActivity {


    private LinearLayout ll_ll;
    private RelativeLayout rl,rl2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_ll = findViewById(R.id.ll_ll);
        rl = findViewById(R.id.rl);
        rl2 = findViewById(R.id.rl2);
//        rl.setRotationX(70);
        rl.setRotationY(50);
        rl2.setRotationY(-50);
//        rl.setY(10);


//        ll_ll.setRotationY(70);



    }

    public void onclik(View view) {
        ActivityAlertDialogManager.displayOneBtnDialog(this, "是", "aa", "确定", "取消", -1, new DialogClick() {
            @Override
            public void CancelClick(DialogInterface dialog, int which) {
                ToastUtils.showShortToast(MainActivity.this, "aaa");
            }

            @Override
            public void ComfirmClick(DialogInterface dialog, int which) {

            }
        }).show();

    }


}
