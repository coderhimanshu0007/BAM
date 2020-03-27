package com.teamcomputers.bam.Interface;

import java.util.regex.Pattern;

public interface BAMConstant {

    int DELAY_MILLIS = 2000;

    //Email Validation Pattern
    Pattern EMAIL_VALIDATION_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    interface ProgressDialogTexts {
        String AUTHENTICATING = "Authenticating...";
        String LOADING = "Loading...";
    }

    interface Constants {
        String FINISH = "finish";
    }

    interface DateFormat {
        String DEFAULT = "dd/MM/yyyy";
        String DEFAULT_YY = "dd/MM/yy";
        String DDMMYYYY = "dd-MM-yyyy";
        String YYYYMMDD = "yyyy-MM-dd";
        String MMDDYYYY = "MM/dd/yyyy";
        String EEE_D_MMM_YYYY = "EEE, d MMM yyyy";
        String HH_MM_SS_A = "hh:mm:ss a";
        String MMMM_DD_EEEE = "MMMM dd, (EEEE)";
        String MMMM = "MMMM";
        String DDMMM = "dd MMM";
        String DMMM = "d MMM";
        String DDMMMYYYY = "dd MMM, yyyy";
        String DDMM = "dd/MM";
        String KK_MM = "kk:mm";
        String HH_MM = "hh:mm a";
        String ATTENDANCE_DATE_FORMAT = "MM/dd/yyyy kk:mm:s a";
        String ATTENDANCE_DATE_FORMAT_1 = "MM/dd/yyyy hh:mm:ss a";
        String CONFERENCE_DATE_FORMAT = "yyyy/MM/dd";
    }

    interface DashBoardFragments {
        String MY_SELF = "MY_SELF";
        String MY_TEAM = "MY_TEAM";
        String NOTIFICATIONS = "NOTIFICATIONS";
    }


    interface Events {
        int NO_INTERNET_CONNECTION = 0;
        int LOGIN_SUCCESSFUL = 1;
        int LOGIN_UN_SUCCESSFUL = 2;
        int WRONG_LOGIN_CREDENTIALS_USED = 3;
        int YOU_ARE_USING_OLDER_VERSION_OF_APP = 4;
        int YOU_ARE_USING_CURRENT_VERSION_OF_APP = 5;
        int OOPS_MESSAGE = 6;
        int ORDER_PROCESSING = 7;
        int PURCHASE = 8;
        int LOGISTICS = 9;
        int INSTALLATION = 10;
        int COLLECTION = 11;
        int OTHERS = 12;
        int WS = 13;
        int NOT_FOUND = 14;

        int GET_ORDERPROCESING_REFRESH_SUCCESSFULL = 30;
        int GET_ORDERPROCESING_REFRESH_UNSUCCESSFULL = 31;
        int GET_ORDERPROCESING_FINANCE_APPROVAL_SUCCESSFULL = 32;
        int GET_ORDERPROCESING_FINANCE_APPROVAL_UNSUCCESSFULL = 33;
        int GET_ORDERPROCESING_SOAUTHORIZATION_SUCCESSFULL = 34;
        int GET_ORDERPROCESING_SOAUTHORIZATION_UNSUCCESSFULL = 35;
        int GET_ORDERPROCESING_SPCSUBMISSION_SUCCESSFULL = 36;
        int GET_ORDERPROCESING_SPCSUBMISSION_UNSUCCESSFULL = 37;
        int GET_ORDERPROCESING_LOWMARGIN_SUCCESSFULL = 38;
        int GET_ORDERPROCESING_LOWMARGIN_UNSUCCESSFULL = 39;

        int GET_PURCHASE_REFRESH_SUCCESSFULL = 40;
        int GET_PURCHASE_REFRESH_UNSUCCESSFULL = 41;
        int GET_PURCHASE_SALES_ORDER_SUCCESSFULL = 42;
        int GET_PURCHASE_SALES_ORDER_UNSUCCESSFULL = 43;
        int GET_PURCHASE_BILLING_SUCCESSFULL = 44;
        int GET_PURCHASE_BILLING_UNSUCCESSFULL = 45;
        int GET_PURCHASE_STOCK_SUCCESSFULL = 46;
        int GET_PURCHASE_STOCK_UNSUCCESSFULL = 47;
        int GET_PURCHASE_EDD_SUCCESSFULL = 48;
        int GET_PURCHASE_EDD_UNSUCCESSFULL = 49;

        int GET_LOGISTICS_REFRESH_SUCCESSFULL = 50;
        int GET_LOGISTICS_REFRESH_UNSUCCESSFULL = 51;
        int GET_LOGISTICS_DISPATCH_SUCCESSFULL = 52;
        int GET_LOGISTICS_DISPATCH_UNSUCCESSFULL = 53;
        int GET_LOGISTICS_INTRANSIT_SUCCESSFULL = 54;
        int GET_LOGISTICS_INTRANSIT_UNSUCCESSFULL = 55;
        int GET_LOGISTICS_HOLD_DELIVERY_SUCCESSFULL = 56;
        int GET_LOGISTICS_HOLD_DELIVERY_UNSUCCESSFULL = 57;
        int GET_LOGISTICS_ACKNOWLEDGEMENT_SUCCESSFULL = 58;
        int GET_LOGISTICS_ACKNOWLEDGEMENT_UNSUCCESSFULL = 59;

        int GET_INSTALLATION_REFRESH_SUCCESSFULL = 60;
        int GET_INSTALLATION_REFRESH_UNSUCCESSFULL = 61;
        int GET_INSTALLATION_OPEN_CALLS_SUCCESSFULL = 62;
        int GET_INSTALLATION_OPEN_CALLS_UNSUCCESSFULL = 63;
        int GET_INSTALLATION_WIP_SUCCESSFULL = 64;
        int GET_INSTALLATION_WIP_UNSUCCESSFULL = 65;
        int GET_INSTALLATION_DOA_IR_SUCCESSFULL = 66;
        int GET_INSTALLATION_DOA_IR_UNSUCCESSFULL = 67;
        int GET_INSTALLATION_HOLD_SUCCESSFULL = 68;
        int GET_INSTALLATION_HOLD_UNSUCCESSFULL = 69;

        int GET_COLLECTION_REFRESH_SUCCESSFULL = 70;
        int GET_COLLECTION_REFRESH_UNSUCCESSFULL = 71;
        int GET_COLLECTION_OUTSTANDING_SUCCESSFULL = 72;
        int GET_COLLECTION_OUTSTANDING_UNSUCCESSFULL = 73;
        int GET_COLLECTION_COLLECTION_SUCCESSFULL = 74;
        int GET_COLLECTION_COLLECTION_UNSUCCESSFULL = 75;
        int GET_COLLECTION_OS_AGEING_SUCCESSFULL = 76;
        int GET_COLLECTION_OS_AGEING_UNSUCCESSFULL = 77;
        int GET_COLLECTION_DELIVERY_INSTALLATION_SUCCESSFULL = 78;
        int GET_COLLECTION_DELIVERY_INSTALLATION_UNSUCCESSFULL = 79;

        int GET_SALES_REFRESH_SUCCESSFULL = 80;
        int GET_SALES_REFRESH_UNSUCCESSFULL = 81;
        int GET_RECEIVABLE_REFRESH_SUCCESSFULL = 82;
        int GET_RECEIVABLE_REFRESH_UNSUCCESSFULL = 83;
        int GET_SALES_RECEIVABLE_SALES_SUCCESSFULL = 84;
        int GET_SALES_RECEIVABLE_SALES_UNSUCCESSFULL = 85;
        int GET_SALES_RECEIVABLE_OUTSTANDING_SUCCESSFULL = 86;
        int GET_SALES_RECEIVABLE_OUTSTANDING_UNSUCCESSFULL = 87;

        int GET_SALES_RECEIVABLE_SUCCESSFULL = 88;
        int GET_SALES_RECEIVABLE_UNSUCCESSFULL = 89;
        int GET_FULL_SALES_LIST_SUCCESSFULL = 90;
        int GET_FULL_SALES_LIST_UNSUCCESSFULL = 91;
        int GET_YTDQTD_SUCCESSFULL = 92;
        int GET_YTDQTD_UNSUCCESSFULL = 93;
        int GET_SALES_PERSON_LIST_SUCCESSFULL = 94;
        int GET_SALES_PERSON_LIST_UNSUCCESSFULL = 95;
        int GET_FULL_CUSTOMER_LIST_SUCCESSFULL = 96;
        int GET_FULL_CUSTOMER_LIST_UNSUCCESSFULL = 97;
        int GET_SELECTED_CUSTOMER_LIST_SUCCESSFULL = 98;
        int GET_SELECTED_CUSTOMER_LIST_UNSUCCESSFULL = 99;
        int GET_FULL_PRODUCT_LIST_SUCCESSFULL = 100;
        int GET_FULL_PRODUCT_LIST_UNSUCCESSFULL = 101;
        int GET_SELECTED_PRODUCT_LIST_SUCCESSFULL = 102;
        int GET_SELECTED_PRODUCT_LIST_UNSUCCESSFULL = 103;
        int GET_FILTER_SALES_LIST_SUCCESSFULL = 104;
        int GET_FILTER_SALES_LIST_UNSUCCESSFULL = 105;
        int GET_OPEN_SALES_ORDER_LIST_SUCCESSFULL = 106;
        int GET_OPEN_SALES_ORDER_LIST_UNSUCCESSFULL = 107;
        int GET_OUTSTANDING_LIST_SUCCESSFULL = 108;
        int GET_OUTSTANDING_LIST_UNSUCCESSFULL = 109;
    }

    interface ToastTexts {
        String LOGIN_SUCCESSFULL = "Login Successfull..";
        String NO_INTERNET_CONNECTION = "No Internet Connection...";
        String NO_RECORD_FOUND = "No Record Found...";
        String WRONG_LOGIN_CREDENTIALS_USED = "Wrong Login Credential...";
        String LOGIN_UNSUCCESSFULL = "Login Unsuccessfull...";
        String OOPS_MESSAGE = "Oops Something went wrong...";
        String WORK_PROGRESS = "Work is in progress...";
        String CANCEL = "CANCEL";
        String UPDATE = "UPDATE";
        String YOU_ARE_USING_OLDER_VERSION_OF_TEAM_WORKS_APP_PLEASE_USE_LATEST_VERSION = "You are using older version of TeamWorks" +
                " app please use latest version.";
        String INFORMATION = "Information";
    }

    interface Fragments {
        int NONE = 0;
        int USER_PROFILE_FRAGMENTS = 101;
        int EDIT_PROFILE_FRAGMENTS = 102;
        int HOME_FRAGMENTS = 103;
        int ORDERPROCESSING_FRAGMENTS = 104;
        int ORDERPROCESSING_FRAGMENTS1 = 1041;
        int ORDERPROCESSING_FRAGMENTS2 = 1042;
        int ORDERPROCESSING_FRAGMENTS3 = 1043;
        int ORDERPROCESSING_FRAGMENTS4 = 1044;
        int PURCHASE_FRAGMENTS = 105;
        int PURCHASE_FRAGMENTS1 = 1051;
        int PURCHASE_FRAGMENTS2 = 1052;
        int PURCHASE_FRAGMENTS3 = 1053;
        int PURCHASE_FRAGMENTS4 = 1054;
        int LOGISTICS_FRAGMENTS = 106;
        int LOGISTICS_FRAGMENTS1 = 1061;
        int LOGISTICS_FRAGMENTS2 = 1062;
        int LOGISTICS_FRAGMENTS3 = 1063;
        int LOGISTICS_FRAGMENTS4 = 1064;
        int INSTALLATION_FRAGMENTS = 107;
        int INSTALLATION_FRAGMENTS1 = 1071;
        int INSTALLATION_FRAGMENTS2 = 1072;
        int INSTALLATION_FRAGMENTS3 = 1073;
        int INSTALLATION_FRAGMENTS4 = 1074;
        int COLLECTION_FRAGMENTS = 108;
        int COLLECTION_FRAGMENTS1 = 1081;
        int COLLECTION_FRAGMENTS2 = 1082;
        int COLLECTION_FRAGMENTS3 = 1083;
        int COLLECTION_FRAGMENTS4 = 1084;
        int OTHERS_FRAGMENTS = 109;
        int SETTINGS_FRAGMENTS = 110;
        int FEEDBACK_FRAGMENTS = 111;
        int SR_FRAGMENTS = 112;
        int SR_FRAGMENTS1 = 1121;
        int SR_FRAGMENTS2 = 1122;

        int RSM_ANALYSIS_FRAGMENT = 113;
        int ACCOUNT_FRAGMENT = 114;
        int CUSTOMER_FRAGMENT = 115;
        int PRODUCT_FRAGMENT = 116;

        int SALES_ANALYSIS_FRAGMENT = 117;
        int CUSTOMER_ANALYSIS_FRAGMENT = 118;

        int WS_RSM_FRAGMENT = 1001;
        int WS_ACCOUNT_FRAGMENT = 1002;
        int WS_CUSTOMER_FRAGMENT = 1003;
        int WS_PRODUCT_FRAGMENT = 1004;

        int OSO_RSM_FRAGMENT = 1005;
        int OSO_ACCOUNT_FRAGMENT = 1006;
        int OSO_CUSTOMER_FRAGMENT = 1007;
        int OSO_INVOICE_FRAGMENT = 1008;

        int TOS_RSM_FRAGMENT = 1009;
        int TOS_ACCOUNT_FRAGMENT = 1010;
        int TOS_CUSTOMER_FRAGMENT = 1011;
        int TOS_PRODUCT_FRAGMENT = 1012;
    }

    interface ClickEvents {
        int RSM_ITEM = 201;
        int ACCOUNT_ITEM = 202;
        int CUSTOMER_ITEM = 203;
        int STATE_ITEM = 204;
        int RSM_CLICK = 205;
        int SP_CLICK = 206;
        int CUSTOMER_SELECT = 207;
        int STATE_SELECT = 208;
        int RSM_MENU_SELECT = 209;
        int CUSTOMER_MENU_SELECT = 210;
        int SP_MENU_SELECT = 211;
        int PRODUCT_MENU_SELECT = 212;
    }

    interface BackpressEvents {
        int R1_BACK_PRESS = 301;
        int R2_BACK_PRESS = 302;
        int R3_BACK_PRESS = 303;
        int R4_BACK_PRESS = 304;
        /*int CUSTOMER_BACK_PRESS = 305;
        int CUSTOMER_BACK_PRESS = 306;
        int CUSTOMER_BACK_PRESS = 307;
        int CUSTOMER_BACK_PRESS = 308;*/
    }
}
