package com.mengqitv.publishsomething.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mengqitv.publishsomething.GlobalContext;
import com.mengqitv.publishsomething.R;

/**
 * Created by weiwei.huang on 2016/10/11.
 */

public class BitmapUtil {
    /**
     * 把返回的str，转换成bitmap,变蓝
     *
     * @param name
     * @return
     */
    public static Bitmap getNameBitmap(String name) {

		/* 把@相关的字符串转换成bitmap 然后使用DynamicDrawableSpan加入输入框中 */

        name = "" + name;
        Paint paint = new Paint();
        paint.setColor(GlobalContext.getInstance().getResources().getColor(R.color.colorPrimary));
        paint.setAntiAlias(true);
        paint.setTextSize(28);
        Rect rect = new Rect();

        paint.getTextBounds(name, 0, name.length(), rect);

        // 获取字符串在屏幕上的长度
        int width = (int) (paint.measureText(name));

        final Bitmap bmp = Bitmap.createBitmap(width, rect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        canvas.drawText(name, rect.left, rect.height() - rect.bottom, paint);


        return bmp;
    }
}
