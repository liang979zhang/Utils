package face.com.zdl.cctools.iostyledialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import face.com.zdl.cctools.R;

public class AlertEditDialog extends BaseDialog {
    private TextView txt_title;
    private TextView txt_msg;
    private EditText et_text;
    private Button leftBtn;
    private Button rightBtn;
    private ImageView img_line;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showLeftBtn = false;
    private boolean showRightBtn = false;

    @SuppressLint("WrongConstant")
    public AlertEditDialog(Context context) {
        super(context);
        this.rootView = LayoutInflater.from(context).inflate(R.layout.view_alert_edit_dialog, (ViewGroup)null);
        this.txt_title = (TextView)this.rootView.findViewById(R.id.txt_title);
        this.txt_title.setVisibility(8);
        this.txt_msg = (TextView)this.rootView.findViewById(R.id.txt_msg);
        this.txt_msg.setVisibility(8);
        this.et_text = (EditText)this.rootView.findViewById(R.id.et_text);
        this.leftBtn = (Button)this.rootView.findViewById(R.id.btn_left);
        this.leftBtn.setVisibility(8);
        this.rightBtn = (Button)this.rootView.findViewById(R.id.btn_right);
        this.rightBtn.setVisibility(8);
        this.img_line = (ImageView)this.rootView.findViewById(R.id.img_line);
        this.img_line.setVisibility(8);
        this.dialog = new Dialog(context, R.style.AlertDialogStyle);
        this.dialog.setContentView(this.rootView);
        this.setScaleWidth(0.85D);
    }

    public AlertEditDialog setCancelable(boolean cancel) {
        this.dialog.setCancelable(cancel);
        return this;
    }

    public AlertEditDialog setScaleWidth(double scWidth) {
        return (AlertEditDialog)super.setScaleWidth(scWidth);
    }

    public AlertEditDialog setTitle(String title) {
        int color = this.mContext.getResources().getColor(R.color.def_title_color);
        return this.setTitle(title, color, 18, true);
    }

    public AlertEditDialog setTitle(String title, int color, int size, boolean isBold) {
        this.showTitle = true;
        this.txt_title.setText(title);
        if (color != -1) {
            this.txt_title.setTextColor(color);
        }

        if (size > 0) {
            this.txt_title.setTextSize((float)size);
        }

        if (isBold) {
            this.txt_title.setTypeface(this.txt_title.getTypeface(), 1);
        }

        return this;
    }

    public AlertEditDialog setMessage(String msg) {
        int color = this.mContext.getResources().getColor(R.color.def_title_color);
        return this.setMessage(msg, color, 16, false);
    }

    public AlertEditDialog setMessage(String msg, int color, int size, boolean isBold) {
        this.showMsg = true;
        this.txt_msg.setText(msg);
        if (color != -1) {
            this.txt_msg.setTextColor(color);
        }

        if (size > 0) {
            this.txt_msg.setTextSize((float)size);
        }

        if (isBold) {
            this.txt_msg.setTypeface(this.txt_msg.getTypeface(), 1);
        }

        return this;
    }

    public AlertEditDialog setRightButton(String text, View.OnClickListener listener) {
        int color = this.mContext.getResources().getColor(R.color.def_title_color);
        return this.setRightButton(text, color, 16, false, listener);
    }

    public AlertEditDialog setRightButton(String text, int color, int size, boolean isBold, final View.OnClickListener listener) {
        this.showRightBtn = true;
        this.rightBtn.setText(text);
        if (color != -1) {
            this.rightBtn.setTextColor(color);
        }

        if (size > 0) {
            this.rightBtn.setTextSize((float)size);
        }

        if (isBold) {
            this.rightBtn.setTypeface(this.rightBtn.getTypeface(), 1);
        }

        this.rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }

                AlertEditDialog.this.dialog.dismiss();
            }
        });
        return this;
    }

    public AlertEditDialog setLeftButton(String text, View.OnClickListener listener) {
        int color = this.mContext.getResources().getColor(R.color.def_title_color);
        return this.setLeftButton(text, color, 16, false, listener);
    }

    public AlertEditDialog setLeftButton(String text, int color, int size, boolean isBold, final View.OnClickListener listener) {
        this.showLeftBtn = true;
        this.leftBtn.setText(text);
        if (color != -1) {
            this.leftBtn.setTextColor(color);
        }

        if (size > 0) {
            this.leftBtn.setTextSize((float)size);
        }

        if (isBold) {
            this.leftBtn.setTypeface(this.leftBtn.getTypeface(), 1);
        }

        this.leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }

                AlertEditDialog.this.dialog.dismiss();
            }
        });
        return this;
    }

    public AlertEditDialog setEditCallListener(final AlertEditDialog.EditTextCallListener callListener) {
        this.et_text.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callListener.callBack(s.toString());
            }

            public void afterTextChanged(Editable s) {
            }
        });
        return this;
    }

    @SuppressLint("WrongConstant")
    private void setLayout() {
        if (!this.showTitle && !this.showMsg) {
            this.txt_title.setText("Alert");
            this.txt_title.setVisibility(0);
        }

        if (this.showTitle) {
            this.txt_title.setVisibility(0);
        }

        if (this.showMsg) {
            this.txt_msg.setVisibility(0);
        }

        if (!this.showRightBtn && !this.showLeftBtn) {
            this.rightBtn.setText("OK");
            this.rightBtn.setVisibility(0);
            this.rightBtn.setBackgroundResource(R.drawable.alertdialog_single_selector);
            this.rightBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertEditDialog.this.dialog.dismiss();
                }
            });
        }

        if (this.showRightBtn && this.showLeftBtn) {
            this.rightBtn.setVisibility(0);
            this.rightBtn.setBackgroundResource(R.drawable.alertdialog_right_selector);
            this.leftBtn.setVisibility(0);
            this.leftBtn.setBackgroundResource(R.drawable.alertdialog_left_selector);
            this.img_line.setVisibility(0);
        }

        if (this.showRightBtn && !this.showLeftBtn) {
            this.rightBtn.setVisibility(0);
            this.rightBtn.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!this.showRightBtn && this.showLeftBtn) {
            this.leftBtn.setVisibility(0);
            this.leftBtn.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

    }

    public void show() {
        this.setLayout();
        this.dialog.show();
    }

    public interface EditTextCallListener {
        void callBack(String var1);
    }
}
