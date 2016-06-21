package com.personal.common.utilities;

/**
 * Created by saurabhagrawal on 21/06/16.
 */
public class CommonUtilities {

    public static boolean isStringEmptyorNull(String check){
        return (check==null || check.isEmpty() || check.equalsIgnoreCase("null")) ;
    }
}
