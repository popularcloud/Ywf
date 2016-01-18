package com.qiuweixin.veface.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Base64;

import com.qiuweixin.veface.base.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class ExtendedBitmapFactory {
    // 图片色值
    public static final Bitmap.Config IN_PREFERRED_CONFIG = Bitmap.Config.RGB_565;
    // 图片压缩格式
    public static final Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    // 图片质量
    public static final int QUALITY = 100;

    public static BitmapFactory.Options getBitmapOption() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = IN_PREFERRED_CONFIG;
        options.inPurgeable = true;
        options.inInputShareable = true;

        return options;
    }

    /**
     *  1:1缩小图片到指定尺寸以下
     * @return 缩小后的图片
     */
    public static Bitmap shrink(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null)
            return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width < maxWidth && height < maxHeight) {
            return bitmap;
        }

        int[] adjust = shrink(width, height, maxWidth, maxHeight);

        return Bitmap.createScaledBitmap(bitmap, adjust[0], adjust[1], true);
    }

    /**
     * 1:1缩小图片到指定尺寸以下
     * @return 计算后得出的尺寸
     */
    public static int[] shrink(float width, float height, int maxWidth, int maxHeight) {
        int newWidth = (int)width;
        int newHeight = (int)height;

        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                float scale = width / maxWidth;
                newWidth = maxWidth;
                newHeight = (int)(height / scale);
            } else {
                float scale = height / maxHeight;
                newHeight = maxHeight;
                newWidth = (int)(width / scale);
            }
        }

        return new int[]{newWidth, newHeight};
    }

    /**
     * 1:1调整图片到指定尺寸
     * @return 计算后得出的尺寸
     */
    public static int[] zoom(float width, float height, int targetWidth, int targetHeight) {
        int newWidth = targetWidth;
        int newHeight = targetHeight;

        if (width != targetWidth && height != targetHeight) {
            if (width > height) {
                float scale = targetWidth / width;
                newWidth = targetWidth;
                newHeight = (int)(height * scale);
            } else {
                float scale = targetHeight / height;
                newHeight = targetHeight;
                newWidth = (int)(width * scale);
            }
        }

        return new int[]{newWidth, newHeight};
    }

    // 从Resource里创建Bitmap
    public static Bitmap createBitmapFromResource(int resId) {
        //获取资源图片
        Bitmap bitmap;
        InputStream is = App.self.getResources().openRawResource(resId);
        bitmap = BitmapFactory.decodeStream(is, null, getBitmapOption());

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap createBitmapFromUri(Context context, Uri uri) throws IOException {
        if (uri == null) {
            return null;
        }

        ContentResolver contentResolver = context.getContentResolver();
        ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
        if (parcelFileDescriptor == null) {
            return null;
        }
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, getBitmapOption());
        parcelFileDescriptor.close();

        return bitmap;
    }

    public static Bitmap createBitmapFromPath(String path) throws IOException {
        return createBitmapFromFile(new File(path));
    }

    public static Bitmap createBitmapFromFile(File file) throws IOException {
        return createBitmapFromFile(file, getBitmapOption());
    }

    public static Bitmap createBitmapFromFile(File file, BitmapFactory.Options options) throws IOException {
        if (file == null) {
            return null;
        }

        FileInputStream fis;
        FileDescriptor fileDescriptor;
        fis = new FileInputStream(file);
        fileDescriptor = fis.getFD();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /* 从指定文件读取图片，并压缩到指定大小以下(不稳定)
     * @return 处理后的Bitmap
     */
    public static Bitmap compressImageFromPath(String path, int kb) throws IOException {
        if (path == null) {
            return null;
        }

        int maxSize = 1024 * kb;

        // 从文件读取图片
        BitmapFactory.Options options = getBitmapOption();
        Bitmap bitmap;
        bitmap = createBitmapFromFile(new File(path), options);

        if (bitmap.getByteCount() <= maxSize) {
            return bitmap;
        }

        //把图片输出成byte[]
        byte[] bytes = toByteArray(bitmap);
        bitmap = null;

        // 计算出压缩倍数
        int bytesSize = bytes.length;
        double dScale = bytesSize / maxSize;
        if (dScale > 1) {
            dScale = Math.sqrt(dScale);
        }
        int scale = (int)dScale;
        if (dScale > scale) {
            scale += 1;
        }

        // 缩放图片
        options.inSampleSize = scale;
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytesSize, options);

        return bitmap;
    }

    /* 从指定文件读取图片，并压缩到指定大小以下
     * @return 处理后的Bitmap
     */
    public static Bitmap compressImageFromPath(String path, int maxWidth, int maxHeight) throws IOException {
        if (path == null) {
            return null;
        }

        // 从文件读取图片
        BitmapFactory.Options options = getBitmapOption();
        Bitmap bitmap = createBitmapFromFile(new File(path), options);

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        if (bWidth <= maxWidth && bHeight <= maxHeight) {
            return bitmap;
        }

        int[] resize = shrink(bWidth, bHeight, maxWidth, maxHeight);
        bitmap = Bitmap.createScaledBitmap(bitmap, resize[0], resize[1], true);

        return bitmap;
    }

    // Bitmap to byte[]
    public static byte[] toByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(COMPRESS_FORMAT, QUALITY, baos);
        byte[] byteArr = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArr;
    }

    public static String toBase64(String path) throws IOException {
        if (path == null) {
            return null;
        }
        File file = new File(path);

        return toBase64(file);
    }

    public static String toBase64(File file) throws IOException {
        if (file == null) {
            return null;
        }
        Bitmap bitmap = createBitmapFromFile(file);

        return toBase64(bitmap);
    }


    /**
     *
     * @param bitmap
     * @return
     */
    public static String toBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        byte[] bytes = ExtendedBitmapFactory.toByteArray(bitmap);

        return formatToBase64(
                COMPRESS_FORMAT.name().toLowerCase(),
                Base64.encodeToString(bytes, Base64.DEFAULT)
        );
    }

    /**
     *
     * @param extension 文件扩展名
     * @param base64Str
     * @return
     */
    public static String formatToBase64(String extension, String base64Str) {
        return "data:" +
                "image/"
                + extension
                + ";base64,"
                + base64Str;
    }
}
