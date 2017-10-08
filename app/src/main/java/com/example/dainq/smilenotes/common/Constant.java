package com.example.dainq.smilenotes.common;

public class Constant {
    public static int SETTING_EVENT_NUMBER = 1;

    /*Realm*/
    public static String REALM_DATA_CUSTOMER_DEFAULT = "customer.realm";

    /*Validate*/
    public static int VALIDATE_SUCCESS = 0;
    public static int VALIDATE_EMPTY = 1;
    public static int VALIDATE_PHONE = 2;

    /*Customer type*/
    public static int CUSTOMER_TYPE_NEW = 0;
    public static int CUSTOMER_TYPE_NEW_MONTH = 1;
    public static int CUSTOMER_TYPE_CONSUMER = 2;
    public static int CUSTOMER_TYPE_DISTRIBUTION = 3;

    /*Customer level*/
    public static int CUSTOMER_LEVEL_0 = 0;
    public static int CUSTOMER_LEVEL_1 = 1;
    public static int CUSTOMER_LEVEL_2 = 2;
    public static int CUSTOMER_LEVEL_3 = 3;
    public static int CUSTOMER_LEVEL_4 = 4;

    /*Customer field*/
    public static String CUSTOMER_NAME = "name";
    public static String CUSTOMER_ADA = "ada";
    public static String CUSTOMER_PHONE_NUMBER = "phonenumber";
    public static String CUSTOMER_DOB = "dateofbirth";

    /*regex*/
    public static String regexPhoneNumber = "^0[0-9]{9,10}$";

    /*customer*/
    public static String KEY_LEVEL_CUSTOMER = "level";
    public static String KEY_ID_CUSTOMER = "id";

    /*pref*/
    public static String PREF_NAME = "pref_customer";
    public static int PREF_ID_DEFAULT = 0;

    /*bundle create*/
    public static String KEY_ACTION = "action";
    public static int ACTION_CREATE = 0;
    public static int ACTION_EDIT = 1;

    /*notification*/
    public static int NOTIFICATION_BIRTH_DAY = 1;
    public static int NOTIFICATION_EVENT = 2;
    public static String NOTIFICATION_TYPE = "notification_type";

    public static int NOTIFICATION_HOUR_DEFAULT = 16;
    public static int NOTIFICATION_MIN_DEFAULT = 20;
    public static int NOTIFICATION_SECOND_DEFAULT = 0;

    /*create*/
    public static String FORMAT_DATE = "dd-MM-yyyy";
}
