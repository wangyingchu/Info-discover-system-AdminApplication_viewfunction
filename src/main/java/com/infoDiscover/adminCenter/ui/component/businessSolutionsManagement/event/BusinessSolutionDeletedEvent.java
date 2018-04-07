package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionDeletedEvent implements Event {

    private String businessSolutionName;

    public BusinessSolutionDeletedEvent(String businessSolutionName){
        this.setBusinessSolutionName(businessSolutionName);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    public interface BusinessSolutionDeletedListener extends Listener {
        public void receivedBusinessSolutionDeletedEvent(final BusinessSolutionDeletedEvent event);
    }
}
