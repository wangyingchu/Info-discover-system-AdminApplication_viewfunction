package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 10/1/16.
 */
public class DiscoverSpaceComponentSelectedEvent implements Event {

    private String discoverSpaceName;

    public DiscoverSpaceComponentSelectedEvent(String discoverSpaceName){
        this.setDiscoverSpaceName(discoverSpaceName);
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public interface DiscoverSpaceComponentSelectedListener extends Listener {
        public void receivedDiscoverSpaceComponentSelectedEvent(final DiscoverSpaceComponentSelectedEvent event);
    }
}
