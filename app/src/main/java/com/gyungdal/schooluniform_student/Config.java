package com.gyungdal.schooluniform_student;

/**
 * Created by GyungDal on 2016-09-09.
 */
public class Config {
    public enum State{SUCCESS, OFFLINE, ERROR, FAIL_AUTH}

    public static final String SERVER_URL = "192.168.144.132/gnuboard5/";
    public static final String SERVER_PROTOCAL = "http://";
    //public static final String SERVER_URL = "gyungdal.xyz/school/";
    public static final String LOGIN_PATH = "bbs/login_check.php";
    public static final String REGISTER_PATH = "bbs/register.php";
    public static final String REGISTER_RESULT_PATH = "bbs/register_result.php";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0)";
}
