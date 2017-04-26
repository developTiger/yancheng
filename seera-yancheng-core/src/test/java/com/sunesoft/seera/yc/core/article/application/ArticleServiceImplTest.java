package com.sunesoft.seera.yc.core.article.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;

import com.sunesoft.seera.yc.core.article.domain.ArticleType;
import com.sunesoft.seera.yc.core.article.domain.criteria.ArticleCriteria;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ArticleServiceImplTest extends TestCase {

    @Autowired
    IArticleService service;

    @Test
    public void testCreate() throws Exception {
        for (int i = 0; i < 5; i++) {
            ArticleDto dto = new ArticleDto();
            dto.setAuthor("x1");
            dto.setContent("c中线程的四种状态");
            dto.setTitle("c");
            dto.setType(ArticleType.HelpCenter);
            CommonResult c = service.create(dto);
            if (c != null) {
                System.out.println(c.getIsSuccess());
            } else System.out.println("null");
        }
    }

    @Test
    public void testEdit() throws Exception {
        ArticleDto dto = new ArticleDto();
        dto.setId(3l);
        dto.setAuthor("xx");
        dto.setTitle("tt");
        CommonResult c = service.edit(dto);
        if (c != null)
            System.out.println(c.getIsSuccess());
        else System.out.println("null");
    }

    @Test
    public void testGetById() throws Exception {
        ArticleDto dto=service.getById(2l);
        if(dto!=null)System.out.println(dto.getId());
    }

    @Test
    public void testGetByIds() throws Exception {
        List<Long> list=new ArrayList<>();
        list.add(1l);
        list.add(2l);
        List<ArticleDto> dtos=service.getByIds(list);
        if(dtos!=null)
            System.out.println(dtos.size());

    }

    @Test
    public void testGetByAuthor() throws Exception {
        List<ArticleDto> dtos=service.getByAuthor("xx");
        if(dtos!=null)
            System.out.println(dtos.size());
    }

    @Test
    public void testGetByType() throws Exception {
        List<ArticleDto> dtos=service.getByType(ArticleType.HelpCenter);
        if(dtos!=null)
            System.out.println(dtos.size());
    }

    @Test
    public void testDelete() throws Exception {
        List<Long> list=new ArrayList<>();
        list.add(1l);
        list.add(2l);
       CommonResult c=service.delete(list);
        if(c!=null)
            System.out.println(c.getIsSuccess());
    }

    @Test
    public void testFindPage() throws Exception {
        ArticleCriteria criteria=new ArticleCriteria();
        criteria.setAuthor("x");
        PagedResult<ArticleDto> pg=service.findPage(criteria);
        if(pg!=null)
            System.out.println(pg.getItems().size()+"\t"+pg.getPageNumber()+"\t"+pg.getPageSize()+"\t"+pg.getTotalItemsCount()+"\t"+pg.getPagesCount());
    }

}