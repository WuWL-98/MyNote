package com.example.mynote.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardUtil {
    public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;//File.separator相当于'/'
    public static String APP_NAME = "MyNote";


    /**
     * 获得文章图片保存路径
     */
    public static String getPictureDir() {
        String imageCacheUrl = SDCardRoot + APP_NAME + File.separator;
        File file = new File(imageCacheUrl);
        if (!file.exists())
            file.mkdir();  //如果不存在则创建
        return imageCacheUrl;
    }

    public static String saveMyBitmap(Bitmap bmp, String bitName) throws IOException {
        File dirFile = new File(getPictureDir());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File f = new File(getPictureDir() + bitName + ".png");
        boolean flag = false;
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fOut.flush();//清空缓冲区，使得数据完全输出
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();//关闭读写流
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

}
