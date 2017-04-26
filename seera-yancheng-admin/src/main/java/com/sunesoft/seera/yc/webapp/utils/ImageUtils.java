package com.sunesoft.seera.yc.webapp.utils;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageUtils {


    /**
     * jpg文件格式
     */
    public static String JPG = "jpg";

    /**
     * png文件格式
     */
    public static String PNG = "png";

    /**
     * 图片裁剪工具
     *
     * @param in              ******************输入流
     * @param readImageFormat *****图片格式
     * @param x               *******************起始点x坐标
     * @param y               *******************起始点y坐标
     * @param w               *******************裁剪宽度
     * @param h               *******************裁剪高度
     * @return is*****************输出流
     * @throws java.io.IOException
     * @author Jieve
     */
    public static InputStream cutImage(InputStream in, String readImageFormat, int x, int y, int w, int h) throws IOException {

        Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(readImageFormat);
        ImageReader reader = (ImageReader) iterator.next();
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, readImageFormat, os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        return is;
    }

    /**
     * 根据图片对象获取对应InputStream
     *
     * @param image
     * @param readImageFormat
     * @return
     * @throws java.io.IOException
     */
    public static InputStream getInputStream(BufferedImage image, String readImageFormat) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, readImageFormat, os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return is;
    }

    /**
     * 生成缩略图
     *
     * @param in
     * @param size
     * @param imageFormat
     * @return
     * @throws java.io.IOException
     */
    public static InputStream createThumbnails(InputStream in, int size, String imageFormat) throws IOException {
        if (imageFormat.equalsIgnoreCase("jpg")) {

            return createThumbnails(in, size);
        } else if (imageFormat.equalsIgnoreCase("png")) {
            BufferedImage image = Thumbnails.of(in).scale(1f).outputFormat(JPG).asBufferedImage();
            InputStream is = getInputStream(image, JPG);
            return createThumbnails(is, size);
        }
        return null;
    }

    /**
     * 转化为jpg格式
     *
     * @param image
     * @return
     * @throws java.io.IOException
     */
    public static BufferedImage toJPG(BufferedImage image) throws IOException {

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage image_ = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = image_.createGraphics();
        graphic.setColor(Color.WHITE);
        graphic.fillRect(0, 0, width, height);
        graphic.drawRenderedImage(image, null);
        graphic.dispose();
        return image_;
    }

    /**
     * 生成缩略图
     *
     * @param in
     * @param width
     * @param height
     * @return
     * @throws java.io.IOException
     */
    public static InputStream createThumbnails(InputStream in, int width, int height) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnailator.createThumbnail(in, os, width, height);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        return is;
    }

    /**
     * 生成缩略图
     *
     * @param in
     * @param size
     * @return
     * @throws java.io.IOException
     */
    public static InputStream createThumbnails(InputStream in, int size) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnailator.createThumbnail(in, os, size, size);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        return is;
    }

    /**
     * 添加水印
     *
     * @param in
     * @param width
     * @param height
     * @return
     * @throws java.io.IOException
     */
    public static InputStream addWaterMark(InputStream in, InputStream waterMark, int width, int height) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(in).size(width, height).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(waterMark), 0.4f).outputQuality(0.8f).toOutputStream(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        waterMark.close();
        os.close();
        return is;
    }

    /**
     * 居中裁剪
     *
     * @param image
     * @return
     */
    public static BufferedImage clipCenter(BufferedImage image) {

        int height = image.getHeight();
        int width = image.getWidth();
        int size = height >= width ? width : height;
        int temp = 0;
        if (height >= width) {
            temp = (height - width) / 2;
            image = image.getSubimage(0, temp, size, size);
        } else {
            temp = (width - height) / 2;
            image = image.getSubimage(temp, 0, size, size);
        }

        return image;
    }


    /**
     * 水平居中裁剪
     *
     * @param image
     * @param x     比例
     * @param y
     * @return
     */
    public static InputStream clipXCenter(BufferedImage image, String imageType, int x, int y) throws IOException {


        int width = image.getWidth();


        int height = image.getHeight();
        int reHight = (width / x) * y;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, imageType, os);
        InputStream is =cutImage(getInputStream(image,imageType),imageType,0,(height-reHight)/2,width,reHight); ;




        return createThumbnails(is,490,210);
    }

    /**
     * 裁剪图片
     *
     * @param image
     * @param x
     * @param y
     * @param size
     * @return
     */
    public static BufferedImage cutImage(BufferedImage image, int x, int y, int size) {

        int height = image.getHeight();
        int width = image.getWidth();
        if ((width >= (x + size)) && (height >= (y + size))) {
            image = image.getSubimage(x, y, size, size);
        } else {
            int temp = ((height - y) >= (width - x)) ? (width - x) : (height - y);
            image = image.getSubimage(x, y, temp, temp);
        }

        return image;
    }

    /**
     * 检查格式是否合法
     *
     * @param imageType
     * @return
     */
    public static boolean checkType(String imageType) {

        boolean flag = false;
        if (JPG.equalsIgnoreCase(imageType) || PNG.equalsIgnoreCase(imageType)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 压缩图片
     * 默认输出50%质量图片
     *
     * @param image
     * @return
     * @throws java.io.IOException
     */
    public static InputStream compress(BufferedImage image, String readImageFormat) throws IOException {

        InputStream in = ImageUtils.getInputStream(image, readImageFormat);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(in).size(image.getWidth(), image.getHeight()).outputQuality(0.5f).toOutputStream(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        return is;
    }
} 