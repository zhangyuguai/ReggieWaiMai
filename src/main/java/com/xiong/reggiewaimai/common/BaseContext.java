package com.xiong.reggiewaimai.common;

/**
 * @author xsy
 * @date 2022/8/11
 */
public class BaseContext {
    private static  ThreadLocal<Long> threadLocal =new ThreadLocal();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
