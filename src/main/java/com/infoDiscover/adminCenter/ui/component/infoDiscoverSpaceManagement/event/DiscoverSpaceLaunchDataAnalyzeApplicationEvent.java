package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 3/2/17.
 */
public class DiscoverSpaceLaunchDataAnalyzeApplicationEvent implements Event {

    private String discoverSpaceName;

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public DiscoverSpaceLaunchDataAnalyzeApplicationEvent(String discoverSpaceName){
        this.setDiscoverSpaceName(discoverSpaceName);
    }

    public interface DiscoverSpaceLaunchDataAnalyzeApplicationListener extends Listener {
        public void receivedDiscoverSpaceLaunchDataAnalyzeApplicationEvent(final DiscoverSpaceLaunchDataAnalyzeApplicationEvent event);
    }
}
