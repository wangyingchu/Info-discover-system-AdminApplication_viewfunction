package com.infoDiscover.adminCenter.ui.util;

import com.vaadin.ui.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangychu on 12/20/16.
 */
public class RuntimeWindowsRepository {

    private Map<String,Map<String,Window>> discoverSpaceWindowsRepositoryInternalMap;

    public RuntimeWindowsRepository(){
        this.discoverSpaceWindowsRepositoryInternalMap=new HashMap<>();
    }

    public Window getExistingWindow(String discoverSpaceName,String windowUID){
        Map<String,Window> discoverSpaceWindowsRepositoryMap=this.discoverSpaceWindowsRepositoryInternalMap.get(discoverSpaceName);
        if(discoverSpaceWindowsRepositoryMap==null){
            return null;
        }else{
            Window targetWindow=discoverSpaceWindowsRepositoryMap.get(windowUID);
            return targetWindow;
        }
    }

    public void addNewWindow(String discoverSpaceName,String windowUID,Window windowInstance){
        Map<String,Window> discoverSpaceWindowsRepositoryMap=this.discoverSpaceWindowsRepositoryInternalMap.get(discoverSpaceName);
        if(discoverSpaceWindowsRepositoryMap==null){
            discoverSpaceWindowsRepositoryMap=new HashMap<String,Window>();
            this.discoverSpaceWindowsRepositoryInternalMap.put(discoverSpaceName,discoverSpaceWindowsRepositoryMap);
        }
        discoverSpaceWindowsRepositoryMap.put(windowUID,windowInstance);
    }

    public void removeExistingWindow(String discoverSpaceName,String windowUID){
        Map<String,Window> discoverSpaceWindowsRepositoryMap=this.discoverSpaceWindowsRepositoryInternalMap.get(discoverSpaceName);
        if(discoverSpaceWindowsRepositoryMap!=null){
            discoverSpaceWindowsRepositoryMap.remove(windowUID);
        }
    }
}
