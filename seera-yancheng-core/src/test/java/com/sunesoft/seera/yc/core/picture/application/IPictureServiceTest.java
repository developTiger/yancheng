package com.sunesoft.seera.yc.core.picture.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.article.application.IArticleService;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.picture.domain.PictureType;
import com.sunesoft.seera.yc.core.picture.domain.criteria.PictureCriteria;
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
public class IPictureServiceTest extends TestCase {

    @Autowired
    IPictureService service;

    @Autowired
    IArticleService iArticleService;

    @Test
    public void testCreate() throws Exception {

        List<ArticleDto> listArticle=iArticleService.getAll();

        for (int i = 1; i < 10; i++) {
            PictureDto dto = new PictureDto();
            dto.setName("x" + i);
            dto.setPath("C:\\xzl\\");
            dto.setLink("https://www.baidu.com/");
            dto.setType(PictureType.banner);
            CommonResult c = service.create(dto);
            if (c.getIsSuccess()) {
                System.out.println(c.getIsSuccess() + "\t" + c.getId());
            } else
                System.out.println(c.getMsg());

        }
    }

    @Test
    public void testEdit() throws Exception {
        PictureDto dto = new PictureDto();
        dto.setId(4l);
        dto.setName("x" + 1);
        dto.setPath("D:\\xzl\\");
        dto.setLink("https://www.baidu.com/");
        dto.setType(PictureType.banner);
        dto.setLink("klhj0i");
        dto.setPosition(1);
        dto.setLocation("s");
        CommonResult c = service.edit(dto);
        if (c.getIsSuccess()) {
            System.out.println(c.getIsSuccess() + "\t" + c.getId());
        } else
            System.out.println(c.getMsg());

    }

    @Test
    public void testGetBySrc() throws Exception {
        List<PictureDto> dtos = service.getBySrc("https://www.baidu.com/");
        if (dtos != null) {
            System.out.println(dtos.size());
        }

    }

    @Test
    public void testGetById() throws Exception {
        PictureDto dto = service.getById(1l);
        if (dto != null)
            System.out.println(dto.getId() + "\t" + dto.getName());
    }

    @Test
    public void testGetByIds() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2l);
        List<PictureDto> dtos = service.getByIds(ids);
        if (dtos != null)
            System.out.println(dtos.size());

    }

    @Test
    public void testRemove() throws Exception {
        CommonResult c = service.remove(1l);
        if (c != null) {
            System.out.println(c.getId() + "\t" + c.getIsSuccess());
        }

    }

    @Test
    public void testRemove1() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(1l);
        ids.add(2L);
        ids.add(3l);
        CommonResult c = service.remove(ids);
        if (c != null)
            System.out.println(c.getIsSuccess());
    }

    @Test
    public void testGetByName() throws Exception {
        List<PictureDto> dtos = service.getByName("x3");
        if (dtos != null)
            System.out.println(dtos.size());
    }

    @Test
    public void testGetByPath() throws Exception {
        List<PictureDto> dtos = service.getByPath("D:\\xzl\\");
        if (dtos != null)
            System.out.println(dtos.size());
    }

    @Test
    public void testGetByType() throws Exception {
        List<PictureDto> dtos = service.getByType(PictureType.banner);
        if (dtos != null)
            System.out.println(dtos.size());
    }

    @Test
    public void testExist() throws Exception {
        CommonResult c = service.exist("x6", "D:\\xzl\\");
        if (c != null)
            System.out.println(c.getId() + "\t" + c.getIsSuccess() + "\t" + c.getMsg());
    }

    @Test
    public void testPage() throws Exception {
        PictureCriteria criteria=new PictureCriteria();
        criteria.setType(PictureType.banner);
        PagedResult<PictureDto> pg=service.page(criteria);
        if(pg!=null)
            System.out.println(pg.getItems().size()+"\t"+pg.getPageNumber()+"\t"+pg.getPageSize()+"\t"+pg.getTotalItemsCount());
    }
}