package com.sunesoft.seera.yc.webapp.controller.information;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.article.application.IArticleService;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;
import com.sunesoft.seera.yc.core.article.domain.ArticleType;
import com.sunesoft.seera.yc.core.article.domain.criteria.ArticleCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.utils.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/21.
 */
@Controller
public class WordEditController extends Layout {

    @Autowired
    IArticleService iArticleService;


    @RequestMapping(value = "sra_t_w")
    public ModelAndView index(Model model){
        return view(layout,"information/index",model);
    }

    @RequestMapping(value = "ajax_wordEdit_query_list")
    public void queryData(HttpServletResponse response,ArticleCriteria articleCriteria,HttpServletRequest request){
        /*List list = new ArrayList<>();
        for(int i=0;i<10;i++){
            WordEdit wordEdit = new WordEdit();
            wordEdit.setAuthority("权限");
            wordEdit.setCount("12");
            wordEdit.setNum("120");
            wordEdit.setPerson("赵子龙");
            wordEdit.setProblemType("问题论文类型");
            wordEdit.setTitle("科技");
            wordEdit.setUpdateTime(new Date());
            list.add(wordEdit);
        }

        PagedResult pagedResult = new PagedResult(list,1,10,1);*/

        String type = request.getParameter("articleType");
        if (type.equals("1")){
            articleCriteria.setType(ArticleType.HelpCenter);
        }
        if (type.equals("2")){
            articleCriteria.setType(ArticleType.ServicePromise);
        }
        if (type.equals("3")){
            articleCriteria.setType(ArticleType.SiteTerm);
        }
        if (type.equals("4")){
            articleCriteria.setType(ArticleType.friendlyLink);
        }

        String author = URI.deURI(request.getParameter("person"));
        if (!StringUtils.isNullOrWhiteSpace(author))
        articleCriteria.setAuthor(author);

        PagedResult pagedResult = iArticleService.findPage(articleCriteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response,json);

    }

    @RequestMapping(value = "_editWordEditForm")
    public ModelAndView editWordEdit(Model model,HttpServletRequest request){
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)){
            ArticleDto articleDto=iArticleService.getById(Long.parseLong(id));

            if (articleDto.getType().equals("HelpCenter")){
                model.addAttribute("type","帮助中心");
            }
            if (articleDto.getType().equals("ServicePromise")){
                model.addAttribute("type","服务承诺");
            }
            if (articleDto.getType().equals("SiteTerm")){
                model.addAttribute("type","网络条款");
            }
            if (articleDto.getType().equals("friendlyLink")){
                model.addAttribute("type","友情链接");
            }

            model.addAttribute("beans",articleDto);
        }
        return view(layout,"information/_editWordEditForm",model);
    }

    @RequestMapping(value = "addArticleForm")
    public ModelAndView addArticle(Model model){
        return view(layout,"information/addArticleForm",model);
    }

    @RequestMapping(value = "ajax_edit_wordEdit",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addArticle(ArticleDto articleDto,HttpServletRequest request){
        String content = request.getParameter("hidContent");
        String type = request.getParameter("articleType");

        if (type.equals("1")){
            articleDto.setType(ArticleType.HelpCenter);
        }
        if (type.equals("2")){
            articleDto.setType(ArticleType.ServicePromise);
        }
        if (type.equals("3")){
            articleDto.setType(ArticleType.SiteTerm);
        }
        if (type.equals("4")){
            articleDto.setType(ArticleType.friendlyLink);
        }

        articleDto.setContent(content);
        CommonResult commonResult=iArticleService.create(articleDto);
        return commonResult;
    }

    @RequestMapping(value = "ajax_edit_wordEditForm",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult editData(ArticleDto articleDto,HttpServletRequest request){
        String time = request.getParameter("time");
        if (!StringUtils.isNullOrWhiteSpace(time)){
            articleDto.setLastUpdateTime(DateHelper.parse(time,"yyyy-MM-dd"));
        }
        String type = request.getParameter("articleType");

        if (type.equals("1")){
            articleDto.setType(ArticleType.HelpCenter);
        }
        if (type.equals("2")){
            articleDto.setType(ArticleType.ServicePromise);
        }
        if (type.equals("3")){
            articleDto.setType(ArticleType.SiteTerm);
        }
        if (type.equals("4")){
            articleDto.setType(ArticleType.friendlyLink);
        }
        CommonResult commonResult = iArticleService.edit(articleDto);
        return commonResult;
    }

    /**
     * 单个删除
     * @param request
     * @return
     */
    @RequestMapping(value = "ajax_delete_wordEdit")
    @ResponseBody
    public CommonResult deleteWordEdit(HttpServletRequest request){
        String id = request.getParameter("id");
        String[] ids = id.split(",");
        List list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            list.add(Long.parseLong(ids[i]));
        }
        CommonResult commonResult=iArticleService.delete(list);
        return commonResult;
    }

    /**
     * 批量删除
     * @param request
     * @return
     */
    @RequestMapping(value = "ajax_deleteWordEdit")
    @ResponseBody
    public CommonResult deleteDataList(HttpServletRequest request){
        String id = request.getParameter("ids");
        String[] ids = id.split(",");
        List list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            list.add(Long.parseLong(ids[i]));
        }
        CommonResult commonResult=iArticleService.delete(list);
        return commonResult;
    }
}
