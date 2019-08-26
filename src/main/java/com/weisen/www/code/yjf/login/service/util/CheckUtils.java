package com.weisen.www.code.yjf.login.service.util;


import com.weisen.www.code.yjf.login.config.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {
    /**
     * 检查电话号码格式和长度,正确返回true
     * @param phone
     * @return
     */
    public static Boolean checkPhoneNumber(String phone){
        Pattern r = Pattern.compile(Constants.REGEX_MOBILE);
        Matcher m = r.matcher(phone.trim());
        if(null != phone.trim() && phone.trim().length() == 11 && m.matches()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检查短信内容是否为空
     * @param content
     * @return
     */
    public static Boolean checkContent(String content){
        if(null != content.trim() && 0 != content.trim().length() && !"".equals(content.trim()))
            return true;
        return false;
    }

    /**
     * 检查用户身份是否为匿名用户或者管理员
     * @return
     */
    public static Boolean checkUser(String account){
//        if (null == account.trim() || "".equals(account.trim()) || "anonymousUser".equals(account.trim()) || "admin".equals(account.trim()) || "internal".equals(account.trim()))
//            return false;
        return true;
    }

    /**
     *
     * @param parm
     * @return
     */
    public static Boolean checkString(String parm){
        if(null == parm.trim() || "".equals(parm.trim()))
            return false;
        return true;
    }

    /**
     * 检查long类型是否为空和0
     * @param parm
     * @return
     */
    public static Boolean checkLongByZero(Long parm){
        if(null == parm || 0 == parm)
            return false;
        return true;
    }

    /**
     * 检查long类型是否为空,但不检查0
     * @param parm
     * @return
     */
    public static Boolean checkLongIsNull(Long parm){
        if(null == parm)
            return false;
        return true;
    }

    /**
     * 检查integer是否为空和null
     * @param parm
     * @return
     */
    public static Boolean checkIntegerByZero(Integer parm){
        if(null == parm || 0 == parm)
            return false;
        return true;
    }

    /**
     * 检查分页信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static Boolean checkPageInfo(Integer pageNum,Integer pageSize){
        if(null == pageNum || null == pageSize || 0 == pageSize || 0 > pageNum){
            return false;
        }
        return true;
    }

    /**
     * 检查对象是否为空,为空返回false
     * @param parm
     * @return
     */
    public static Boolean checkObj(Object parm){
        if(null == parm){
            return false;
        }
        return true;
    }

    /**
     * 检查List集合是否为空
     * @param list
     * @return
     */
    public static Boolean checkList(List<?> list){
        if(null == list ||0 == list.size() || list.contains(null))
            return false;
        return true;
    }

    /**
     * 检查数组是否为空
     * @param parm
     * @return
     */
    public static Boolean checkArray(Object[] parm){
        if(null == parm || 0 == parm.length || Arrays.asList(parm).contains(null))
            return false;
        return true;
    }

}
