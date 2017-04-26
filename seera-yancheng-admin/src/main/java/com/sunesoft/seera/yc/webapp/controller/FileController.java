package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.yc.core.fileSys.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhouz on 2016/8/23.
 */
@Controller
public class FileController {

    @Autowired
    FileService fileService;

    @RequestMapping(value = "img/{path}.{ext}")
    public void getImage(@PathVariable String path,@PathVariable String ext, HttpServletRequest request, HttpServletResponse response) {
        try {
            path = path.replace("_","/");
            InputStream inStream = fileService.getFile(path+"."+ext);//通过输入流获取图片数据
            byte data[] = readInputStream(inStream);
            inStream.read(data);  //读数据

            response.setContentType("image/jpg"); //设置返回的文件类型
            OutputStream os = response.getOutputStream();
            os.write(data);
            inStream.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        return outStream.toByteArray();
    }
}
