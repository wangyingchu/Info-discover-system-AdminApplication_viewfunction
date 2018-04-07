package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionComponentSelectedEvent implements Event {
    private String businessSolutionName;

    public BusinessSolutionComponentSelectedEvent(String businessSolutionName){
        this.setBusinessSolutionName(businessSolutionName);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    public interface BusinessSolutionComponentSelectedListener extends Listener {
        public void receivedBusinessSolutionComponentSelectedEvent(final BusinessSolutionComponentSelectedEvent event);
    }
}
