package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

import java.util.Stack;

/**
 * Created by wangychu on 5/8/17.
 */
public class RelationablesPathVO {

    private Stack<RelationValueVO> pathRelationRoute;
    private String endPointRelationableAId;
    private String endPointRelationableBId;

    public Stack<RelationValueVO> getPathRelationRoute() {
        return pathRelationRoute;
    }

    public void setPathRelationRoute(Stack<RelationValueVO> pathRelationRoute) {
        this.pathRelationRoute = pathRelationRoute;
    }

    public String getEndPointRelationableAId() {
        return endPointRelationableAId;
    }

    public void setEndPointRelationableAId(String endPointRelationableAId) {
        this.endPointRelationableAId = endPointRelationableAId;
    }

    public String getEndPointRelationableBId() {
        return endPointRelationableBId;
    }

    public void setEndPointRelationableBId(String endPointRelationableBId) {
        this.endPointRelationableBId = endPointRelationableBId;
    }
}
