package com.sunesoft.seera.yc.core;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

/**
 * Created by zhouz on 2016/8/31.
 */
public class ImageResize {


//    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) throws IOException
//    {
//        int type = source.getType();
//        BufferedImage target = null;
//        double sx = (double) targetW / source.getWidth();
//        double sy = (double) targetH / source.getHeight();
//        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
//        // 则将下面的if else语句注释即可
//        if (sx > sy)
//        {
//            sx = sy;
//            targetW = (int) (sx * source.getWidth());
//        }
//        else
//        {
//            sy = sx;
//            targetH = (int) (sy * source.getHeight());
//        }
//        if (type == BufferedImage.TYPE_CUSTOM)
//        { // handmade
//            ColorModel cm = source.getColorModel();
//            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
//                    targetH);
//            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
//            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
//        }
//        else
//        {
//            //固定宽高，宽高一定要比原图片大
//            //target = new BufferedImage(targetW, targetH, type);
//            target = new BufferedImage(targetW, targetH, type);
//        }
//
//        Graphics2D g = target.createGraphics();
//
//        //写入背景
////        g.drawImage(ImageIO.read(new File("D:\\yc_img\\blank.png")), 0, 0, null);
//
//        // smoother than exlax:
//        g.setRenderingHint(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
//        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
//        g.dispose();
//
//        return target;
//    }


    public static void main(String[] args) throws IOException
    {
        BufferedImage bimage= ImageIO.read(new File("D:\\yc_img\\p6.jpg"));
        InputStream image = ImageUtils.getInputStream(bimage,"jpg");

        InputStream  mobileImage = ImageUtils.reSize(bimage,500);
        FileUtils.copyInputStreamToFile(mobileImage, new File("D:\\yc_img\\p61.jpg"));
        image.close();
    }
}
