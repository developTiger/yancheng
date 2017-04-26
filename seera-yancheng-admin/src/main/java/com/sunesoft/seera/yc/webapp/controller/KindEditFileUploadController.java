package com.sunesoft.seera.yc.webapp.controller;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.yc.core.fileSys.FileService;
import com.sunesoft.seera.yc.core.fileSys.FileType;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

;

/**
* Created by zhouz on 2016/9/6.
*/
@Controller
public class KindEditFileUploadController {

    @Autowired
    FileService fileService;


    @RequestMapping(value = "kindEditUpload", method = RequestMethod.POST)
    @ResponseBody
    public void kindEditorUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String dirName =request.getParameter("dir");
        String fileName = "";
        CommonResult result;

        String imgHost = Configs.getProperty("imgHost", "http://127.0.0.1:8033");

        //最大文件大小
        long maxSize = 10000000;
        response.setContentType("text/html; charset=UTF-8");

        if (!ServletFileUpload.isMultipartContent(request)) {
            returnKindEditorMsg(getError("请选择文件。"), request, response);
            return;
        }


        //定义允许上传的文件扩展名
        HashMap extMap = new HashMap();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

        //设置响应给前台内容的PrintWriter对象
        PrintWriter out = response.getWriter();
        //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        List<MultipartFile> images = ((DefaultMultipartHttpServletRequest) request).getFiles("imgFile");
        for (MultipartFile myfile : images) {
            if (myfile.isEmpty()) {
                returnKindEditorMsg(getError("请选择文件。"), request, response);
                return;

            } else {
                try {
                    //检查文件大小
                    if (myfile.getSize() > maxSize) {
                        returnKindEditorMsg(getError("上传文件大小超过限制。"), request, response);
                        return;
                    }
                    String originalFilaName = myfile.getOriginalFilename();
                    String extension = originalFilaName.substring(originalFilaName.lastIndexOf(".") + 1);
                    if (!Arrays.asList(extMap.get(dirName).toString().split(",")).contains(extension)) {
                        returnKindEditorMsg(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"), request, response);
                        return;
                    }
                    fileName = DateHelper.formatDate(new Date(), "yyyyMMddhhmmSSsss");
                    fileName = fileName + "." + extension;
                    fileService.upload(fileName, dirName, myfile.getInputStream(), FileType.Oth);


                    Map resultMap = new HashMap<>();
                    resultMap.put("error", 0);

                    resultMap.put("url", imgHost + "/" + dirName + "/" + fileName);
                    returnKindEditorMsg(JsonHelper.toJson(resultMap), request, response);
                    return;
                } catch (IOException e) {
                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
                    e.printStackTrace();
                    out.print("1`文件上传失败，请重试！！");
                    out.flush();

                    returnKindEditorMsg(getError("文件上传失败，请重试！"), request, response);
                    return;
                }
            }
        }
    }

    @RequestMapping(value = "kindEdit_file_manager", method = RequestMethod.GET)
    public void fileManager(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext application = request.getSession().getServletContext();
//        ServletOutputStream out = response.getOutputStream();
        // 根目录路径，可以指定绝对路径，比如 /var/www/upload/   String imgHost = Configs.getProperty("imgHost", "http://127.0.0.1:8033");
        String rootPath =Configs.getProperty("filePath", "D:\\yc_img\\");
        // 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/upload/
        String rootUrl = Configs.getProperty("imgHost", "http://127.0.0.1:8033/");
        // 图片扩展名
        String[] fileTypes = new String[] {"gif", "jpg", "jpeg", "png", "bmp"};

        String dirName = request.getParameter("dir");
        if (dirName != null) {
            if (!Arrays.<String>asList(new String[] {"image", "flash", "media", "file"}).contains(
                    dirName)) {
//                out.println("Invalid Directory name.");
                return;
            }
            rootPath += dirName + "\\";
            rootUrl = rootUrl+ "/"+dirName + "/";
            File saveDirFile = new File(rootPath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
        }
        // 根据path参数，设置各路径和URL
        String path = request.getParameter("path") != null ? request.getParameter("path") : "";
        String currentPath = rootPath + path;
        String currentUrl = rootUrl +"/"+ path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath =
                    str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }
        // 排序形式，name or size or type
        String order =
                request.getParameter("order") != null ? request.getParameter("order").toLowerCase()
                        : "name";
        // 不允许使用..移动到上一级目录
        if (path.indexOf("..") >= 0) {
//            out.println("Access is not allowed.");
            return;
        }
        // 最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
//            out.println("Parameter is not valid.");
            return;
        }
        // 目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if (!currentPathFile.isDirectory()) {
//            out.println("Directory does not exist.");
            return;
        }
        // 遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt =
                            fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

        if ("size".equals(order)) {
            Collections.sort(fileList, new SizeComparator());
        } else if ("type".equals(order)) {
            Collections.sort(fileList, new TypeComparator());
        } else {
            Collections.sort(fileList, new NameComparator());
        }
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("moveup_dir_path", moveupDirPath);
        msg.put("current_dir_path", currentDirPath);
        msg.put("current_url", currentUrl);
        msg.put("total_count", fileList.size());
        msg.put("file_list", fileList);
//        response.setContentType("application/json; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        AjaxResponse.write(response, JsonHelper.toJson(msg));
        return;
    }
    @SuppressWarnings("rawtypes")
    class NameComparator implements Comparator {
        public int compare(Object a, Object b) {
            Hashtable hashA = (Hashtable) a;
            Hashtable hashB = (Hashtable) b;
            if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
                return 1;
            } else {
                return ((String) hashA.get("filename")).compareTo((String) hashB.get("filename"));
            }
        }
    }

    @SuppressWarnings("rawtypes")
    class SizeComparator implements Comparator {
        public int compare(Object a, Object b) {
            Hashtable hashA = (Hashtable) a;
            Hashtable hashB = (Hashtable) b;
            if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
                return 1;
            } else {
                if (((Long) hashA.get("filesize")) > ((Long) hashB.get("filesize"))) {
                    return 1;
                } else if (((Long) hashA.get("filesize")) < ((Long) hashB.get("filesize"))) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    class TypeComparator implements Comparator {
        public int compare(Object a, Object b) {
            Hashtable hashA = (Hashtable) a;
            Hashtable hashB = (Hashtable) b;
            if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
                return 1;
            } else {
                return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
            }
        }
    }

    private String getError(String message) {

        Map resultMap = new HashMap<>();
        resultMap.put("error", 1);

        resultMap.put("message", message);

        return JsonHelper.toJson(resultMap);
    }

    private void returnKindEditorMsg(String returnMsg, HttpServletRequest request, HttpServletResponse response) {
//            response.setContentType("text/html;charset=utf-8");
        response.setContentType("application/json; charset=UTF-8");
        try {
            response.getWriter().print(returnMsg);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
