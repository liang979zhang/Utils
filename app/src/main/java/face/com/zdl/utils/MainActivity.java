package face.com.zdl.utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import face.com.zdl.cctools.ToastUtils;
import face.com.zdl.cctools.dialog.ActivityAlertDialogManager;
import face.com.zdl.cctools.dialog.DialogClick;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
