package com.sunesoft.seera.yc.core.fileSys;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.ImageUtils;
import com.sunesoft.seera.fr.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by zhouz on 2016/8/23.
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    private String fileBasePath = Configs.getProperty("filePath", "D:\\yc_img\\");

    @Override
    public CommonResult upload(String fileName, String subPath, InputStream stream, FileType type) {
        String path = fileBasePath;
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (!StringUtils.isNullOrWhiteSpace(subPath)) {
            path += subPath + "\\";
        }

        if (FileType.ProductImage == type) {
            try {
                if(extension.toLowerCase().equals("jpg")||extension.toLowerCase().equals("png")) {
                    //原图
                    File file = new File(path + "orignal\\", fileName);
                    FileUtils.copyInputStreamToFile(stream, file);
                    BufferedImage bimage = ImageIO.read(file);
                    //移动端图片
                    InputStream mobileImage = ImageUtils.clipXCenter(bimage, "jpg", 5, 3);//按比例切图
                    //页面展示图
                    InputStream normal = ImageUtils.createThumbnails(ImageUtils.getInputStream(bimage, extension), 500, 300);
                    //缩略图
                    InputStream suoluo = ImageUtils.createThumbnails(ImageUtils.getInputStream(bimage, extension), 200, 120);
                    FileUtils.copyInputStreamToFile(normal, new File(path, fileName));
                    FileUtils.copyInputStreamToFile(mobileImage, new File(path + "m\\", fileName));
                    FileUtils.copyInputStreamToFile(suoluo, new File(path + "s\\", fileName));

                }
                else {
                    return new CommonResult(false, "文件类型不支持,请上传jpg\\png 类型图片！");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new CommonResult(false, "文件上传失败！");
            }
      //  } else if (FileType.ProductImage == type) {

        } else if(FileType.WxUpload == type){
            try {
                if(extension.toLowerCase().equals("jpg")||extension.toLowerCase().equals("jpeg")||extension.toLowerCase().equals("png")) {
                    if(extension.toLowerCase().equals("jpeg"))
                        extension="jpg";
                    //原图
                    File file = new File(path + "wxOrignal\\", fileName);
                    FileUtils.copyInputStreamToFile(stream, file);
                    BufferedImage bimage = ImageIO.read(file);
                    //移动端图片
                    InputStream  mobileImage = ImageUtils.reSize(bimage,500,extension);
                    FileUtils.copyInputStreamToFile(mobileImage, new File(path + "wx\\", fileName));
                }
                else {
                    return new CommonResult(false, "文件类型不支持,请上传jpg\\png 类型图片！");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new CommonResult(false, "文件上传失败！");
            }
        }else {
            try {
                File file = new File(path, fileName);
                FileUtils.copyInputStreamToFile(stream, file);
            } catch (IOException e) {
                return new CommonResult(false, "文件上传失败！");
            }
        }
        return ResultFactory.commonSuccess();
    }

    @Override
    public FileInputStream getFile(String path) {

        try {
            File file = new File(fileBasePath + path);
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                return in;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }
}
