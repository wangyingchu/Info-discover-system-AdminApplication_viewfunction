package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionCreatedEvent implements Event {

    private String businessSolutionName;

    public BusinessSolutionCreatedEvent(String businessSolutionName){
        this.setBusinessSolutionName(businessSolutionName);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    public interface BusinessSolutionCreatedListener extends Listener {
        public void receivedBusinessSolutionCreatedEvent(final BusinessSolutionCreatedEvent event);
    }
}
