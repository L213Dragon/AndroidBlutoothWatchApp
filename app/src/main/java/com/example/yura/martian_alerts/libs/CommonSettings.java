package com.example.yura.martian_alerts.libs;

/**
 * Created by House on 2015/6/10.
 */
public final class CommonSettings {

	
	/* Trace Log */

    public static final String LOG_TAG  = "Funapps.MARTAIN";
	public static int LOGLEVEL = 4;
	public static boolean ASSERT = LOGLEVEL > -2;
	public static boolean ERROR = LOGLEVEL > -1;
	public static boolean WARN = LOGLEVEL > 0;
	public static boolean INFO = LOGLEVEL > 1;
	public static boolean DEBUG = LOGLEVEL > 2;
	public static boolean VERBOSE = LOGLEVEL > 3;
	
	public final static int ONE_SEC = 1000;
	

	public static final String SERVER_HOST = "";

	/**
	 * DateTime Format
	 */

	public static final String FORMAT_DATETIME_DEFAULT = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_DATE_DEFAULT = "yyyy-MM-dd";
	public static final String FORMAT_TIME_DEFAULT = "HH:mm";





}
