package com.oldfather;

/**
 * Created by theoldfather on 2/7/17.
 */
public class DebugTools {

    public static void printActiveMethodName(){
        StackTraceElement[] st = (new Throwable()).getStackTrace();
        System.out.println(st[1].getMethodName());
    }

    public static void printActiveClassName(){
        StackTraceElement[] st = (new Throwable()).getStackTrace();
        System.out.println(st[1].getClassName());
    }

    public static void printActiveClassMethodName(){
        StackTraceElement[] st = (new Throwable()).getStackTrace();
        System.out.printf("%s.%s()\n",st[1].getClassName(),st[1].getMethodName());
    }



}
