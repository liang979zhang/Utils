package face.com.zdl.cctools.dialog;

import android.content.DialogInterface;

/**
 * Created by Administrator on 2018/5/22.
 */

public interface DialogClick {

    void CancelClick(DialogInterface dialog, int which);

    void ComfirmClick(DialogInterface dialog, int which);
}
