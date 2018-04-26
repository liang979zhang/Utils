package face.com.zdl.cctools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/11/19
 *     desc  : SpannableString工具类
 * </pre>
 */
public class SpannableStringUtils {

    private SpannableStringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取特定的文本
     *
     * @param text
     * @param span
     * @return
     */
    public static SpannableString getSpan(String text, CharacterStyle span) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(span, 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 获取点击文本
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public static SpannableString getOnClickSpan(String text, View.OnClickListener onClickListener) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new SimpleClickableSpan(onClickListener), 0,
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 获取超连接文本
     *
     * @param text
     * @param url
     * @return
     */
    public static SpannableString getUrlSpan(String text, String url, SimpleURLSpan.UrlSpanOnClickListener urlSpanOnClickListener) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new SimpleURLSpan(url, urlSpanOnClickListener), 0,
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 获取背景颜色文本
     *
     * @param text
     * @param color
     * @return
     */
    public static SpannableString getBgColorSpan(String text, int color) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new BackgroundColorSpan(color), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 获取颜色文本
     *
     * @param text
     * @param color
     * @return
     */
    public static SpannableString getColorSpan(String text, int color) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ForegroundColorSpan(color), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 获取字体大小文本
     *
     * @param text
     * @param size
     * @return
     */
    public static SpannableString getFontSpan(String text, int size) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new AbsoluteSizeSpan(size), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 获取字体样式，Typeface
     *
     * @param text
     * @param style
     * @return
     */
    public static SpannableString getStyleSpan(String text, int style) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new StyleSpan(style), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 删除线文本
     *
     * @param text
     * @return
     */
    public static SpannableString getStrikeSpan(String text) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new StrikethroughSpan(), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 下划线文本
     *
     * @param text
     * @return
     */
    public static SpannableString getUnderLineSpan(String text) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new UnderlineSpan(), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 获取图片文本
     *
     * @param context
     * @param text
     * @param bitmap
     * @return
     */
    public static SpannableString getImageSpan(Context context, String text, Bitmap bitmap) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ImageSpan(context, bitmap), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    public static SpannableString getImageSpan(Context context, String text, int resid) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ImageSpan(context, resid), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    public static SpannableString getImageSpan(Context context, String text, Uri uri) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ImageSpan(context, uri), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    public static class SimpleClickableSpan extends ClickableSpan {
        private View.OnClickListener onClickListener;

        public SimpleClickableSpan() {
        }

        public SimpleClickableSpan(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (null != onClickListener) {
                onClickListener.onClick(widget);
            }
        }
    }

    public static class SimpleURLSpan extends URLSpan {
        private final UrlSpanOnClickListener urlSpanOnClickListener;

        public SimpleURLSpan(String url, UrlSpanOnClickListener urlSpanOnClickListener) {
            super(url);
            this.urlSpanOnClickListener = urlSpanOnClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (null != urlSpanOnClickListener) {
                urlSpanOnClickListener.onClick(widget, getURL());
            } else {
                super.onClick(widget);
            }
        }

        /**
         * 点击事件
         *
         * @author xuan
         * @version $Revision: 1.0 $, $Date: 2013-11-11 上午11:33:49 $
         */
        public interface UrlSpanOnClickListener {
            public void onClick(View widget, String url);
        }
    }

}

