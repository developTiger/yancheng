package com.sunesoft.seera.yc.webapp.model;

import com.sunesoft.seera.yc.core.article.application.IArticleService;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;
import com.sunesoft.seera.yc.core.article.domain.ArticleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by temp on 2016/9/7.
 */

@Component
public class HelpCenterFactory {

    @Autowired
    private IArticleService iArticleService;

    private static Map<ArticleType,List<ArticleDto>> mapHC=new HashMap<>();

   /* private static Map<ArticleType, Map<ArticleType,List<ArticleDto>>> mapHC=new HashMap<>();*/

    /*
    public Map<ArticleType,List<ArticleDto>> getAllArticle(){
        ArticleSorts();
        return mapHC;
    }
    */

    public List<ArticleDto> getArticlesByType(ArticleType articleType){
        if(mapHC.size()==0) {
            ArticleSorts();
        }
        return mapHC.get(articleType);
    }

    private void ArticleSorts(){
        List<ArticleDto> listArticle=iArticleService.getAll();
        for(ArticleDto article:listArticle){
            List<ArticleDto> listTemp = mapHC.get(article.getType());
            if(listTemp!=null){
                listTemp.add(article);
                mapHC.put(article.getType(),listTemp);
            }else{
                listTemp=new ArrayList<>();
                listTemp.add(article);
                mapHC.put(article.getType(),listTemp);
            }
        }

    }

}
    


   /* private List<ArticleDto> articleDtoList;

    public HelpCenterFactory(List<ArticleDto> list) {
        this.articleDtoList=list;
    }

    public List<ArticleDto> getListHelpCenter() {
        return getArticleByType(ArticleType.HelpCenter);
    }

    public List<ArticleDto> getListServicePromise() {
        return getArticleByType(ArticleType.ServicePromise);
    }
    public List<ArticleDto> getListSiteTerm() {
        return getArticleByType(ArticleType.SiteTerm);
    }

    private List<ArticleDto> getArticleByType(ArticleType articleType){
        List<ArticleDto> listServicePromise=new ArrayList<>();

            for (ArticleDto article : articleDtoList) {
                if (article.getType().equals(articleType)) {
                    if (listServicePromise.size() >= 4) {
                        break;
                    }
                    listServicePromise.add(article);
                }
            }
            return listServicePromise;

    }

    public List<ArticleDto> getArticleDtoList() {
        return articleDtoList;
    }

    public void setArticleDtoList(List<ArticleDto> articleDtoList) {
        this.articleDtoList = articleDtoList;
    }*/

