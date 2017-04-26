package com.sunesoft.seera.yc.core.activity.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.application.dtos.ShareActivityDto;
import com.sunesoft.seera.yc.core.activity.domain.FollowUser;
import com.sunesoft.seera.yc.core.activity.domain.ShareActivity;
import com.sunesoft.seera.yc.core.activity.domain.ShareActivityRepository;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ShareActivityCriteria;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwork on 2016/12/7.
 */
@Service("shareActivityService")
public class ShareActivityServiceImpl extends GenericHibernateFinder implements ShareActivityService {
    @Autowired
    ShareActivityRepository shareActivityRepository;

    @Override
    public CommonResult addShare(ShareActivityDto dto) {
        if (isExist(dto.getOpenId())) {
            return ResultFactory.commonError("分享已提交，请勿重复提交！");
        }
        ShareActivity activity = new ShareActivity();
        DtoFactory.convert(dto, activity);
        Long id = shareActivityRepository.save(activity);
        return ResultFactory.commonSuccess(id);
    }

    private Boolean isExist(String openId) {
        Criteria criteria = getSession().createCriteria(ShareActivity.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.eq("openId", openId));
        int totalCount = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalCount > 0;
    }

    private ShareActivity getByOpenId(String openId) {
        Criteria criteria = getSession().createCriteria(ShareActivity.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.eq("openId", openId));
        List<ShareActivity> activities = criteria.list();
        if (activities != null && activities.size() > 0)
            return activities.get(0);
        else return null;
    }

    @Override
    public ShareActivityDto getShare(String sharedUserAppId) {
        ShareActivity activity = getByOpenId(sharedUserAppId);


        if(activity==null)
            return null;
        ShareActivityDto dto = convertShare(activity);
        return dto;
    }

    @Override
    public CommonResult likeShare(String inComeOpenId, String inComeWxName, String sharedUserAppId) {
        ShareActivity activity = getByOpenId(sharedUserAppId);
        if(activity==null)return null;
        if(!inComeOpenId.equals(sharedUserAppId)){
            if(activity.getFollowUsers().stream().noneMatch(x->x.getOpenId().equals(inComeOpenId))){
                FollowUser user = new FollowUser();
                user.setOpenId(inComeOpenId);
                user.setWxName(inComeWxName);
                activity.getFollowUsers().add(user);

                shareActivityRepository.save(activity);
            }
            else{
                return ResultFactory.commonError("已点赞，不能重复点赞哦！");
            }
        }
        return ResultFactory.commonSuccess();
    }


    private  ShareActivityDto convertShare(ShareActivity activity){
        ShareActivityDto dto = new ShareActivityDto();
        DtoFactory.convert(activity,dto);

        if(activity.getFollowUsers()!=null)
            dto.setFollowCount(activity.getFollowUsers().size());
        else dto.setFollowCount(0);

        return dto;


    }
    @Override
    public PagedResult<ShareActivityDto> getShareDtoPaged(ShareActivityCriteria criteria) {

        String sql = "select a.id,a.openId,a.wxName,a.title,a.content,a.activityName,a.filePath,a.create_datetime createDateTime,count(a.id) followCount  from share_activity a left join follow_user b on a.id = b.share_id ";
        String countSql = "select count(*) from share_activity a ";

        String whereCondition = "where a.is_active=1 ";


        if (!StringUtils.isNullOrWhiteSpace(criteria.getActivityName())) {
            whereCondition = whereCondition+" and a.activityName like '%"+criteria.getActivityName()+"%'";

        }

        if (!StringUtils.isNullOrWhiteSpace(criteria.getWxName())) {
            whereCondition = whereCondition+" and a.wxName like '%"+criteria.getWxName()+"%'";
        }

        SQLQuery count= getSession().createSQLQuery(countSql + whereCondition);
        SQLQuery query= getSession().createSQLQuery(sql + whereCondition + " GROUP BY a.id order by followCount desc");

        int totalCount =Integer.parseInt(count.uniqueResult().toString()) ;

        query.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());


        addScalar(query, ShareActivityDto.class);
        query.setResultTransformer(Transformers.aliasToBean( ShareActivityDto.class));

        List<ShareActivityDto> activityDtos =query.list();



        return new PagedResult<ShareActivityDto>(activityDtos, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }

    @Override
    public Boolean isLiked(String inComeOpenId, String sharedUserAppId) {
        ShareActivity activity = getByOpenId(sharedUserAppId);
        if(activity==null)return false;
        if(!inComeOpenId.equals(sharedUserAppId)){
          return activity.getFollowUsers().stream().anyMatch(x->x.getOpenId().equals(inComeOpenId));

        }
        return false;
    }
}

