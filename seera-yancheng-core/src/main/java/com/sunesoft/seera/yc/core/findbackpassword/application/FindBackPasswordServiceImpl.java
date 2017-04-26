package com.sunesoft.seera.yc.core.findbackpassword.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.msg.ChannelType;
import com.sunesoft.seera.fr.msg.MessageService;
import com.sunesoft.seera.fr.msg.Msg;
import com.sunesoft.seera.fr.msg.email.EmailMessage;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.MD5;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.findbackpassword.application.factory.FindBackPasswordFactory;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.ITouristRepository;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
/**
 * 游客服务接口实现类
 * Created by zhaowy on 2016/7/11.
 */
@Service("findBackPassword")
public class FindBackPasswordServiceImpl
        extends GenericHibernateFinder implements FindBackPasswordService {

    @Autowired
    MessageService messageService;

    @Autowired
    ITouristRepository iTouristRepository;

    @Override
    public CommonResult checkAccount(TouristCriteria criteria) {
        Criteria criterion = getSession().createCriteria(Tourist.class);
        //region token param
        if (!StringUtils.isNullOrWhiteSpace(criteria.getToken())) {

           /* Criterion hsEmail = Restrictions.eq("email",criteria.getToken());
            Criterion hsUserName = Restrictions.eq("userName",criteria.getToken());
            Criterion hsMobile = Restrictions.eq("mobilePhone",criteria.getToken());
            LogicalExpression tokenHs = Restrictions.or(hsUserName, hsMobile);
            tokenHs = Restrictions.or(tokenHs, hsUserName);
            tokenHs = Restrictions.or(tokenHs, hsMobile);
            tokenHs = Restrictions.or(tokenHs, hsEmail);*/
            criterion.add(Restrictions.eq("email",criteria.getToken()));
        }

        List<Tourist> list = criterion.list();

        return new CommonResult(true,"",list.get(0).getId());
    }

    @Override
    public TouristDto checkAccount(String email) {
        Criteria criterion = getSession().createCriteria(Tourist.class);
        //region token param
        if (!StringUtils.isNullOrWhiteSpace(email)) {

           /* Criterion hsEmail = Restrictions.eq("email",criteria.getToken());
            Criterion hsUserName = Restrictions.eq("userName",criteria.getToken());
            Criterion hsMobile = Restrictions.eq("mobilePhone",criteria.getToken());
            LogicalExpression tokenHs = Restrictions.or(hsUserName, hsMobile);
            tokenHs = Restrictions.or(tokenHs, hsUserName);
            tokenHs = Restrictions.or(tokenHs, hsMobile);
            tokenHs = Restrictions.or(tokenHs, hsEmail);*/
            criterion.add(Restrictions.eq("email",email));
        }
        List<Tourist> list = criterion.list();
        Tourist tourist=list.get(0);
        return FindBackPasswordFactory.conertToTouristDto(tourist);
    }

    @Override
    public boolean send(String email) {
        Msg msg=new EmailMessage();

        messageService.sendMessage(ChannelType.Email, msg);
        return true;
    }

    @Override
    public CommonResult checkApprovedFindBack(String key,String token) {

        Criteria criterion = getSession().createCriteria(Tourist.class);
        criterion.add(Restrictions.eq("userName",token));
        List<Tourist> list=criterion.list();
        if(list==null || list.size()<=0)
            return new CommonResult(false,"无此用户");
        else {
            String tk=list.get(0).getFindpasswordKey();
            String date=tk.substring(tk.indexOf("|")+1,tk.length());
            Date date1= DateHelper.parse(date);
            Long leftTime=new Date().getTime()-date1.getTime();
            if(leftTime/(1000*60)>=30)
                return new CommonResult(false,"邮件链接已超时");;
            if(MD5.GetMD5Code(list.get(0).getFindpasswordKey()).equals(key)){
                return new CommonResult(true);
            }
            return new CommonResult(true);
        }

    }

    @Override
    public CommonResult updatePassword(String userName, String password) {
        Criteria criterion = getSession().createCriteria(Tourist.class);
        criterion.add(Restrictions.eq("userName",userName));
        List<Tourist> list=criterion.list();
        if(list==null || list.size()<=0)
            return new CommonResult(false);
        else {
            Tourist tourist=list.get(0);
            tourist.setPassword(MD5.GetMD5Code(password));
            iTouristRepository.save(tourist);
            return new CommonResult(true);
        }

    }
}
