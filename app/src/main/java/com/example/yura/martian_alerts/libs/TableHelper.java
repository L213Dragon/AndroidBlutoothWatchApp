package com.example.yura.martian_alerts.libs;


/**
 * Created by House on 2015/6/10.
 */
public class TableHelper {
    public static class TableName {
        public static final String ALERTS_SETTINGS = "alertsettings";
    }

    public static class VIPGroup {
        public static final String[] Field =
                {
                        VIPGroup.Id,
                        VIPGroup.GroupName,
                        VIPGroup.GroupColor,
                        VIPGroup.IsEnable,
                        VIPGroup.CreateTime
                };
        public static final String Id = "Id";
        public static final String GroupName = "GroupName";
        public static final String GroupColor = "GroupColor";
        public static final String IsEnable = "IsEnable";
        public static final String CreateTime = "CreateTime";
    }



}
