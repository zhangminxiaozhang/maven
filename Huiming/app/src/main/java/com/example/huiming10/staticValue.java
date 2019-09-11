package com.example.huiming10;

public class staticValue {
    private static String ip;
    private static int scThread;
    private static int curThread;

    public staticValue(String ip_,int scThread_, int curThread_){
        ip = ip_;
        scThread = scThread_;
        curThread = curThread_;
    }

    public static void setIp(String ip_){
        ip = ip_;
    }

    public static String getIp(){
        if(ip == null){
            return "0.0.0.0";
        }else {
            return ip;
        }
    }

    public static void setScThread(int scThread_){
        scThread = scThread_;
    }

    public static int getScThread(){
        return scThread;
    }

    public static void setCurThread(int curThread_){
        curThread = curThread_;
    }

    public static int getCurThread(){
        return curThread;
    }
}
