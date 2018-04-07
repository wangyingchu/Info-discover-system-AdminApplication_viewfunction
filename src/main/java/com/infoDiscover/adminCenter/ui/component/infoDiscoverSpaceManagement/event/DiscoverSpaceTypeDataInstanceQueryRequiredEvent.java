package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 10/26/16.
 */
public class DiscoverSpaceTypeDataInstanceQueryRequiredEvent implements Event {

    private String discoverSpaceName;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getDataInstanceTypeKind() {
        return dataInstanceTypeKind;
    }

    public void setDataInstanceTypeKind(String dataInstanceTypeKind) {
        this.dataInstanceTypeKind = dataInstanceTypeKind;
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    public interface DiscoverSpaceTypeDataInstanceQueryRequiredListener extends Listener {
        public void receivedDiscoverSpaceTypeDataInstanceQueryRequiredEvent(final DiscoverSpaceTypeDataInstanceQueryRequiredEvent event);
    }
}
