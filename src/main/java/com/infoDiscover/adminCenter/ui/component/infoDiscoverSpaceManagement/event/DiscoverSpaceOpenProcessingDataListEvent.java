package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 12/15/16.
 */
public class DiscoverSpaceOpenProcessingDataListEvent implements Event {

    private String discoverSpaceName;

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public DiscoverSpaceOpenProcessingDataListEvent(String discoverSpaceName){
        this.setDiscoverSpaceName(discoverSpaceName);
    }

    public interface DiscoverSpaceOpenProcessingDataListListener extends Listener {
        public void receivedDiscoverSpaceOpenProcessingDataListEvent(final DiscoverSpaceOpenProcessingDataListEvent event);
    }
}
