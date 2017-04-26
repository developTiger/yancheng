package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.model.testObj;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by zhouz on 2016/6/29.
 */
@Controller
public class demonController  extends Layout {

    @Autowired
    UserSession us;


    @RequestMapping(value = "_mutiselect")
    public ModelAndView mutiSelect(Model model) {

        return view( layout,"demon/mutiselect", model);
    }

    @RequestMapping(value = "_fuxuan")
    public ModelAndView fuSelect(Model model) {

        return view(layout, "demon/fuxuan", model);
    }

    @RequestMapping(value = "ajax_fuxuan")
    public ModelAndView addfuSelect1(Model model) {

       // CommonResult result= formKindManagerService.getFormService("test").Add();

        System.out.println("1111111111111");
        return view(layout, "demon/_fuxuan", model);
    }
    @RequestMapping(value = "fileupload")
    public ModelAndView fileUpload(Model model) {

        return view( layout,"demon/fileupload", model);
    }



    @RequestMapping(value="/ajax_fileupload")
    public String addUser(testObj uname,HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("收到用户[" + uname.getUname() + "]的文件上传请求");
        //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
        //这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
        String realPath = request.getSession().getServletContext().getRealPath("/upload");
        //设置响应给前台内容的数据格式
        response.setContentType("text/plain; charset=UTF-8");
        //设置响应给前台内容的PrintWriter对象
        PrintWriter out = response.getWriter();
        //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        List<MultipartFile>  myfiles =((DefaultMultipartHttpServletRequest) request).getFiles("myfiles");
        //如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
        //如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
        //上传多个文件时,前台表单中的所有<input type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
        for(MultipartFile myfile : myfiles){
            if(myfile.isEmpty()){
                out.print("1`请选择文件后上传");
                out.flush();
                return null;
            }else{
                originalFilename = myfile.getOriginalFilename();
                System.out.println("文件原名: " + originalFilename);
                System.out.println("文件名称: " + myfile.getName());
                System.out.println("文件长度: " + myfile.getSize());
                System.out.println("文件类型: " + myfile.getContentType());
                System.out.println("========================================");
                try {
                    //这里不必处理IO流关闭的问题,因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
                    //此处也可以使用Spring提供的MultipartFile.transferTo(File dest)方法实现文件的上传
                    FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, originalFilename));
                } catch (IOException e) {
                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
                    e.printStackTrace();
                    out.print("1`文件上传失败，请重试！！");
                    out.flush();
                    return null;
                }
            }
        }
        //此时在Windows下输出的是[D:\Develop\apache-tomcat-6.0.36\webapps\AjaxFileUpload\\upload\愤怒的小鸟.jpg]
        //System.out.println(realPath + "\\" + originalFilename);
        //此时在Windows下输出的是[/AjaxFileUpload/upload/愤怒的小鸟.jpg]
        //System.out.println(request.getContextPath() + "/upload/" + originalFilename);
        //不推荐返回[realPath + "\\" + originalFilename]的值
        //因为在Windows下<img src="file:///D:/aa.jpg">能被firefox显示,而<img src="D:/aa.jpg">firefox是不认的
        out.print("0`" + request.getContextPath() + "/upload/" + originalFilename);
        out.flush();
        return null;
    }
}
