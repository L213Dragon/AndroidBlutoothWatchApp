package com.example.yura.martian_alerts.libs;


import android.content.Context;
import android.os.Process;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by House on 2015/6/10.
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	public static String THREAD = "ExceptionHandler.ThreadName";
	public static String MESSAGE = "ExceptionHandler.Message";

	private final Context mContext;
	
	public ExceptionHandler(Context context) {
		 mContext = context;
	}
	@Override
	public void uncaughtException(Thread thread, Throwable exception) {

		StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);
		
		
		if(CommonSettings.DEBUG) {
			THREAD = thread.getName();
			MESSAGE = stackTrace.toString();
			Toast.makeText(mContext, THREAD + " : " + MESSAGE, Toast.LENGTH_LONG).show();
		}

		Process.killProcess(Process.myPid());
		System.exit(10);

	}
	

}
