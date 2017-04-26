package com.sunesoft.seera.yc.core.activity.domain.creteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.ActivityType;

import java.util.Date;

/**
 * Created by xiazl on 2016/8/6.
 */
public class ShareActivityCriteria extends PagedCriteria {

    /**
     * 活动名称
     */
    private String activityName;


  private String wxName;



    //region Description  getter an setter

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

//endregion
}
