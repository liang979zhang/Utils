package face.com.zdl.cctools.Permission;

/**
 * Created by 89667 on 2018/3/14.
 */

public class PermissionInfo {

    private String mName;
    private String mShortName;

    public PermissionInfo(String name) {
        this.mName = name;
        this.mShortName = name.substring(name.lastIndexOf(".") + 1);
    }

    public String getName() {
        return mName;
    }


    public void setName(String mName) {
        this.mName = mName;
    }

    public String getShortName() {
        return mShortName;
    }
}
