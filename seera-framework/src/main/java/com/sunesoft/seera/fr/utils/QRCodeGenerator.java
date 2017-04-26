package com.sunesoft.seera.fr.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

/**
 * 二维码生成工具类
 * Created by zhaowy on 2016/8/15.
 */
public class QRCodeGenerator {
    /**
     * 二维码生成器
     *
     * @param text 二维码内容
     *             <p>返回值需要自行增加 <img src="data:image/jpg;base64,returnvalue" /></p>
     */
    @SuppressWarnings({"unchecked", "rawtypes", "restriction"})
    public static String generalQRCode(String text, int width, int height) {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        String binary = null;

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    text, BarcodeFormat.QR_CODE, width, height, hints);


            // 实现二：生成二维码图片并将图片转为二进制传递给前台
            // 1、读取文件转换为字节数组
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage image = toBufferedImage(bitMatrix);
            ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();

            // 2、将字节数组转为二进制
            BASE64Encoder encoder = new BASE64Encoder();
            binary = encoder.encodeBuffer(bytes).trim();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return binary;
    }

    public static String encode(String contents, int width, int height, String imgPath) {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        String binary = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.CODE_128, codeWidth, height, hints);

            // 实现二：生成二维码图片并将图片转为二进制传递给前台
            // 1、读取文件转换为字节数组
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage image = toBufferedImage(bitMatrix);
            ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();

            // 2、将字节数组转为二进制
            BASE64Encoder encoder = new BASE64Encoder();
            binary = encoder.encodeBuffer(bytes).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return binary;
    }


    // 其他调用方法
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

}
