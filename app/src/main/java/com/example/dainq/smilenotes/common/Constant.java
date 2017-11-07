package com.example.dainq.smilenotes.common;

public class Constant {
    public static int SETTING_EVENT_NUMBER = 1;

    /*Realm*/
    public static String REALM_DATA_CUSTOMER_DEFAULT = "customer.realm";

    /*Validate*/
    public static int VALIDATE_SUCCESS = 0;
    public static int VALIDATE_EMPTY = 1;
    public static int VALIDATE_PHONE = 2;
    public static int VALIDATE_ADA = 3;
    public static int VALIDATE_SCHEDULE_TIME = 4;

    /*Customer type*/
    public static int CUSTOMER_TYPE_NEW = 0;
    public static int CUSTOMER_TYPE_NEW_MONTH = 1;
    public static int CUSTOMER_TYPE_CONSUMER = 2;
    public static int CUSTOMER_TYPE_DISTRIBUTION = 3;

    /*Customer level*/
    public static int CUSTOMER_LEVEL_0 = 0; //tiem nang
    public static int CUSTOMER_LEVEL_1 = 1; //tieu dung
    public static int CUSTOMER_LEVEL_2 = 2; //tdtc
    public static int CUSTOMER_LEVEL_3 = 3; //npp
    public static int CUSTOMER_LEVEL_4 = 4; //npptc

    /*Customer field*/
    public static String CUSTOMER_NAME = "name";
    public static String CUSTOMER_DATE_CREATE = "datecreate";
    public static String CUSTOMER_ADA = "ada";
    public static String CUSTOMER_PHONE_NUMBER = "phonenumber";
    public static String CUSTOMER_LEVEL = "level";

    /*ProductAdapter field*/
    public static String PRODUCT_USE_DATE = "usedate";

    /*Notification field*/
    public static String NOTIFICATION_READ = "isread";
    public static String NOTIFICATION_DATE = "date";

    /*regex*/
    public static String regexPhoneNumber = "^0[0-9]{9,10}$";

    /*key*/
    public static String KEY_ID = "id";
    public static String KEY_ID_PLAN = "idcustomer";
    public static String KEY_ID_PRODUCT = "idcustomer";
    public static String KEY_TYPE_PRODILE = "type_prof";

    /*Profile*/
    public static int PROFILE_TYPE_PRODUCT = 1;
    public static int PROFILE_TYPE_PLAN = 2;

    /*pref*/
    public static String PREF_USER = "pref_user";
    public static String USER_AVATAR = "avatar";
    public static String USER_NAME = "name";
    public static String USER_FIRST_USE = "first";
    public static String USER_NOTIFICATION = "notification";
    public static String PREF_NAME = "pref_customer";
    public static String PREF_PLAN = "pref_plan";
    public static int PREF_ID_DEFAULT = 0;

    /*bundle create*/
    public static String KEY_ACTION = "action";
    public static int ACTION_CREATE = 0;
    public static int ACTION_EDIT = 1;

    /*notification*/
    public static int NOTIFICATION_BIRTH_DAY = 1;
    public static int NOTIFICATION_EVENT = 2;
    public static String NOTIFICATION_TYPE = "notification_type";
    public static String NOTIFICATION_NAME_CUSTOMER = "notification_name_customer";
    public static String NOTIFICATION_ID = "id_notification";

    public static String NOTIFICATION_ID_MEETING = "idmeeting";
    public static String NOTIFICATION_ID_CUSTOMER = "idcustomer";

    /*create*/
    public static String FORMAT_DATE = "yyyy-MM-dd";

    /*dialog*/
    public static int DIALOG_CREATE = 10;
    public static int DIALOG_VIEW = 11;
    public static int DIALOG_EDIT = 12;
}
