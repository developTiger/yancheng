package com.sunesoft.seera.yc.webapp.controller.helpercenter;

import com.sunesoft.seera.yc.core.article.application.IArticleService;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhouz on 2016/6/29.
 */
@Controller
public class HelperCenterController extends Layout {

    @Autowired
    IArticleService iArticleService;

    //进入帮助中心
    @RequestMapping(value = "/helpCenter")
    public ModelAndView helpCenter(Model model) {


        return view(activeLayout, "/helpercenter/helpCenter", model);
    }

/*    @RequestMapping(value = "_addOrUpArticle")
    public ModelAndView addArticle( Model model,HttpServletRequest request) {
        String id=request.getParameter("id");
        if(!StringUtils.isNullOrWhiteSpace(id)){
            ArticleDto dto=service.getById(Long.parseLong(id));
            model.addAttribute("bean",dto);
        }
        model.addAttribute("helper", Helper.class);
        ArticleCriteria criteria=new ArticleCriteria();
        criteria.setPageSize(1000);
        PagedResult<ArticleDto> page=service.findPage(criteria);
        model.addAttribute("articles",page);
        return view(activeLayout, "/_addOrUpdateArticle", model);
    }

    @RequestMapping(value = "ajax_add_update_article",method = RequestMethod.POST)
    public void ajax_add_update_Article(ArticleDto dto,HttpServletResponse response) {
        CommonResult result=null;
        if(dto.getId()==null){
            result=service.create(dto);
        }else {
            result=service.edit(dto);
        }
        String json= JsonHelper.toJson(result);
        AjaxResponse.write(response,json);
    }

    @RequestMapping(value = "/helperCenter/detail/{pid}")
    public ModelAndView detail(@PathVariable Long pid, Model model) {
        if (pid != null) {
            ArticleDto dto = service.getById(pid);
            model.addAttribute("article", dto);
        } else {
            model.addAttribute("article", "该文章已经不存在");
        }
        return view(activeLayout, "helpercenter/单个文章页面", model);
    }

    @RequestMapping(value = "ajax_delete_articles")
    public void ajax_delete_article(HttpServletRequest request,HttpServletResponse response) {
        List<Long> list = new ArrayList<>();
        String ids=request.getParameter("ids");
        if(!StringUtils.isNullOrWhiteSpace(ids)){
            for(String id:ids.split(",")){
                list.add(Long.parseLong(id));
            }
            CommonResult c=service.delete(list);
            String json=JsonHelper.toJson(c);
            AjaxResponse.write(response,json);
        }
    }*/

    //点击小标题获取分页查询
    @RequestMapping(value = "/helpCenter/{num}")
    public ModelAndView page(@PathVariable String num, Model model) {
        ArticleDto articleDto=iArticleService.getByNum(num);
        model.addAttribute("article",articleDto);
        return view(activeLayout, "helpercenter/helpCenter", model);
    }

}
