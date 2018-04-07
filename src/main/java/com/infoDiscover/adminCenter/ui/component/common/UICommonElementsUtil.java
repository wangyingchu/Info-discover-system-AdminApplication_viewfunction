package com.infoDiscover.adminCenter.ui.component.common;


import com.vaadin.icons.VaadinIcons;

/**
 * Created by wangychu on 11/11/16.
 */
public class UICommonElementsUtil {

    public static String generateMovableWindowTitleWithFormat(String titleText){
        String resultTitle= VaadinIcons.MODAL.getHtml()+"<span style='font-weight:160;color:#197de1;font-size:17px'>"+" "+titleText.trim()+"</span>";
        return resultTitle;
    }

    public static boolean checkContainsSpecialChars(String checkValue){
        if(checkValue.contains("`")){return true;}
        if(checkValue.contains("=")){return true;}
        if(checkValue.contains(",")){return true;}
        if(checkValue.contains(";")){return true;}
        if(checkValue.contains(":")){return true;}
        if(checkValue.contains("\"")){return true;}
        if(checkValue.contains("'")){return true;}
        if(checkValue.contains(".")){return true;}
        if(checkValue.contains("<")){return true;}
        if(checkValue.contains(">")){return true;}
        if(checkValue.contains("[")){return true;}
        if(checkValue.contains("]")){return true;}
        if(checkValue.contains(" ")){return true;}
        if(checkValue.contains("&")){return true;}
        return false;
    }

    public static boolean checkIsSingleByteString(String str) {
        boolean isSingleByteString=true;
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                if (!checkChar(str.charAt(i))) {
                    isSingleByteString=false;
                    break;
                } else {
                    isSingleByteString = true;
                }
            }
        }
        return isSingleByteString;
    }

    private static boolean checkChar(char ch) {
        if ((ch + "").getBytes().length == 1) {
            return true;//单字节字符
        } else {
            return false;//多字节字符
        }
    }
}
