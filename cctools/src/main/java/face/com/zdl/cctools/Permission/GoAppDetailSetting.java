package face.com.zdl.cctools.Permission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import face.com.zdl.cctools.AppUtils;

/**
 * Created by 89667 on 2018/3/14.
 */

public class GoAppDetailSetting {


    static String SCHEME = "package";
    //调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
    static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    //调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
    static final String APP_PKG_NAME_22 = "pkg";
    //InstalledAppDetails所在包名
    static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    //InstalledAppDetails类名
    static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    public static void goSettng(Context context) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口

            if (CheckPhoneSystemUtils.isMIUI()) {
                Log.i("GoAppDetailSetting", "产品/硬件的制造商小米:");
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", AppUtils.getAppPackageName(context));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "只有MIUI才可以设置哦", Toast.LENGTH_SHORT).show();
                }
            } else if (CheckPhoneSystemUtils.isFlyme()) {
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", AppUtils.getAppPackageName(context));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "只有Flyme才可以设置哦", Toast.LENGTH_SHORT).show();
                }
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts(SCHEME, AppUtils.getAppPackageName(context), null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            if (CheckPhoneSystemUtils.isMIUI()) {
                Log.i("GoAppDetailSetting", "产品/硬件的制造商小米:");
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", AppUtils.getAppPackageName(context));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "只有MIUI才可以设置哦", Toast.LENGTH_SHORT).show();
                }
            } else if (CheckPhoneSystemUtils.isFlyme()) {
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", AppUtils.getAppPackageName(context));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "只有Flyme才可以设置哦", Toast.LENGTH_SHORT).show();
                }
            } else {
                final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                        : APP_PKG_NAME_21);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                        APP_DETAILS_CLASS_NAME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(appPkgName, AppUtils.getAppPackageName(context));
                context.startActivity(intent);
            }


        }


    }

    /*
    检测手机类型
     */
    private static class CheckPhoneSystemUtils {
        private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

        /**
         * 检测MIUI
         *
         * @return
         */
        public static boolean isMIUI() {
            try {
                final BuildProperties prop = BuildProperties.newInstance();
                return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
            } catch (final IOException e) {
                return false;
            }
        }

        /**
         * 检测Flyme
         *
         * @return
         */
        public static boolean isFlyme() {
            try { // Invoke Build.hasSmartBar()
                final Method method = Build.class.getMethod("hasSmartBar");
                return method != null;
            } catch (final Exception e) {
                return false;
            }
        }
    }

    /**
     * 判断手机类型工具类
     */
    public static class BuildProperties {
        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }
    }
}
