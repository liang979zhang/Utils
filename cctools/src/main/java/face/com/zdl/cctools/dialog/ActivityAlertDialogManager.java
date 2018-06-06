package face.com.zdl.cctools.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2018/4/26.
 */

public class ActivityAlertDialogManager {


    //==========常量==========
    private static final String TAG = "ActivityADManager";

    //==========普通静态变量==========
    private static AlertDialog sAlertDialog;                        // 一个Activity下只产生一个AlertDialog实例
    private static AlertDialog.Builder sBuilder;                        // 一个Activity下只产生一个AlertDialog.Builder实例
    private static Activity sLastActivity = null;

    //==========AlertDialog==========//
    public static AlertDialog displayOneBtnDialog(@NonNull Activity Activity, String title, String msg,String sureBtnText,String cancelBtnText,int iconResId, DialogClick dialogClick) {
        if (TextUtils.isEmpty(msg)) return null;
        TipInfo tipInfo = TipInfo.createTipInfo(title, msg,sureBtnText,cancelBtnText,iconResId);
        if (sAlertDialog == null) {
            sAlertDialog = displayOneBtnDialog(Activity, tipInfo, dialogClick);
        } else {

            sAlertDialog = displayOneBtnDialog(Activity, tipInfo, dialogClick);
        }
        return sAlertDialog;
    }

    public static AlertDialog displayOneBtnDialog(@NonNull Activity Activity, TipInfo tipInfo, DialogClick dialogClick) {
        if (tipInfo == null) return null;
        AlertDialog.Builder builder = getBuilder(Activity, tipInfo);
        builder = addAlertDialogNegativeButton(tipInfo.sureBtnText, dialogClick, builder);

        // 显示出该对话框
        sAlertDialog = builder.create();
//        sAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", sureListener);
        if (sAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE) != null) {
            sAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
        }
        return sAlertDialog;
    }

    public static AlertDialog displayTwoBtnDialog(@NonNull Activity Activity, String title, String msg,String sureBtnText,String cancelBtnText,int iconResId, DialogClick dialogClick) {
        if (TextUtils.isEmpty(msg)) return null;
        TipInfo tipInfo = TipInfo.createTipInfo(title, msg, sureBtnText, cancelBtnText, iconResId);
        if (sAlertDialog == null) {
            sAlertDialog = displayTwoBtnDialog(Activity, tipInfo, dialogClick);
        } else {

            sAlertDialog = displayTwoBtnDialog(Activity, tipInfo, dialogClick);
//            sAlertDialog.setTitle(title);
//            sAlertDialog.setMessage(msg);
//            if (sAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE) != null) {
//                sAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
//                sAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
//            }
        }
        return sAlertDialog;
    }

    public static AlertDialog displayTwoBtnDialog(@NonNull Activity Activity, TipInfo tipInfo,DialogClick dialogClick) {
        if (tipInfo == null) return null;
        // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = getBuilder(Activity, tipInfo);
        builder = addAlertDialogPositiveButton(tipInfo.sureBtnText, dialogClick, builder);
        builder = addAlertDialogNegativeButton(tipInfo.cancelBtnText, dialogClick, builder);
        // 显示出该对话框
        sAlertDialog = builder.show();
        return sAlertDialog;
    }

    @NonNull
    private static AlertDialog.Builder getBuilder(@NonNull Activity Activity, TipInfo tipInfo) {
        AlertDialog.Builder builder;
        if (Activity == sLastActivity) {
            if (sBuilder != null) {
                builder = sBuilder;
            } else {
                builder = createNewBuilder(Activity);
            }
        } else {
            reset();
            builder = createNewBuilder(Activity);
            sLastActivity = Activity;
            sBuilder = builder;
        }
        // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        // 设置Title的图标
        if (tipInfo.iconResId != -1) {
            builder.setIcon(tipInfo.iconResId);
        }

        // 设置Title的内容
        builder.setTitle(tipInfo.title);
        // 设置Content来显示一个信息
        builder.setMessage(tipInfo.msg);
        return builder;
    }

    private static void reset() {
        sBuilder = null;
        sAlertDialog = null;
        sLastActivity = null;
    }

    @NonNull
    private static AlertDialog.Builder createNewBuilder(@NonNull Activity Activity) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(Activity);
        sBuilder = builder;
        return builder;
    }

    private static AlertDialog.Builder addAlertDialogPositiveButton(String btnText, final DialogClick dialogClick, final AlertDialog.Builder builder) {
//        listener = getDefaultOnClickListener(listener);
        // 设置一个PositiveButton
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogClick.ComfirmClick(dialog,which);
            }
        });



        return builder;
    }

    private static AlertDialog.Builder addAlertDialogNegativeButton(String btnText, final DialogClick dialogClick, final AlertDialog.Builder builder) {
//        listener = getDefaultOnClickListener(listener);
        // 设置一个PositiveButton
        builder.setNegativeButton("sd", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogClick.CancelClick(dialog, which);
            }
        });
        return builder;
    }

    @NonNull
    private static DialogInterface.OnClickListener getDefaultOnClickListener(DialogInterface.OnClickListener listener) {
        if (listener != null) return listener;

        listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "AlertDialog Button Click!");
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.e("tag", "取消");
                        break;

                    case DialogInterface.BUTTON_POSITIVE:
                        Log.e("tag", "确定");
                        break;
                }
            }
        };
        return listener;
    }
    //==========AlertDialog==========//


    //==========逻辑方法==========//
    public static void destory(@NonNull Activity Activity) {
        if (Activity != sLastActivity) {
            Activity = null;
            return;
        }
        if (sAlertDialog != null) {
            sAlertDialog.cancel();
        }
        Activity = null;
        reset();
    }

}
