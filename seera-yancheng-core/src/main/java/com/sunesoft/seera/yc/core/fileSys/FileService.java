package com.sunesoft.seera.yc.core.fileSys;

import com.sunesoft.seera.fr.results.CommonResult;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by zhouz on 2016/7/6.
 */
public interface FileService {

    /**
     * 上传文件
     * @param fileName 文件名
     * @param subPath 子目录
     * @param stream 文件流
     * @return
     */
    public CommonResult upload(String fileName,String subPath,InputStream stream,FileType type);

    /**
     * 文件路径 （子目录/文件名）
     * @param path（子目录/文件名）
     * @return OutputStream
     */
    public FileInputStream getFile(String path);


}
