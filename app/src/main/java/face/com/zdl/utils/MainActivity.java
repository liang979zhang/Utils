package face.com.zdl.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;
import face.com.zdl.cctools.iostyledialog.ActionSheetDialog;
import face.com.zdl.cctools.iostyledialog.AlertEditDialog;
import face.com.zdl.utils.evnentBind.MessageEvent;


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

        QrConfig qrConfig = new QrConfig.Builder()
                .setDesText("(识别二维码)")//扫描框下文字
                .setShowDes(false)//是否显示扫描框下面文字
                .setShowLight(true)//显示手电筒按钮
                .setShowTitle(true)//显示Title
                .setShowAlbum(true)//显示从相册选择按钮
                .setCornerColor(Color.WHITE)//设置扫描框颜色
                .setLineColor(Color.WHITE)//设置扫描线颜色
                .setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
                .setScanType(QrConfig.TYPE_QRCODE)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
                .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE)//设置扫描框类型（二维码还是条形码，默认为二维码）
                .setCustombarcodeformat(QrConfig.BARCODE_I25)//此项只有在扫码类型为TYPE_CUSTOM时才有效
                .setPlaySound(true)//是否扫描成功后bi~的声音
                .setDingPath(R.raw.test)//设置提示音(不设置为默认的Ding~)
                .setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
                .setTitleText("扫描二维码")//设置Tilte文字
                .setTitleBackgroudColor(Color.TRANSPARENT)//设置状态栏颜色
                .setTitleTextColor(Color.BLACK)//设置Title文字颜色
                .create();
        QrManager.getInstance().init(qrConfig).startScan(this, new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }, new QrManager.LeadTextCallBack() {
            @Override
            public void clickOk() {

            }
        });




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
