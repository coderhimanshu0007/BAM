package com.teamcomputers.bam.Interface

import java.util.regex.Pattern

public interface KBAMConstant {
    val DELAY_MILLIS: Int
        get() = 2000

    //Email Validation Pattern
     val EMAIL_VALIDATION_PATTERN: Pattern
        get() = Pattern.compile(
               "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
               "\\@" +
               "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
               "(" +
               "\\." +
               "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
               ")+"
   )

    interface ProgressDialogTexts {
        companion object {
            val AUTHENTICATING = "Authenticating..."
            val LOADING = "Loading..."
        }
    }

    interface Constants {
        companion object {
            val FINISH = "finish"
        }
    }

    interface DateFormat {
        companion object {
            val DEFAULT = "dd/MM/yyyy"
            val DEFAULT_YY = "dd/MM/yy"
            val DDMMYYYY = "dd-MM-yyyy"
            val YYYYMMDD = "yyyy-MM-dd"
            val MMDDYYYY = "MM/dd/yyyy"
            val EEE_D_MMM_YYYY = "EEE, d MMM yyyy"
            val HH_MM_SS_A = "hh:mm:ss a"
            val MMMM_DD_EEEE = "MMMM dd, (EEEE)"
            val MMMM = "MMMM"
            val DDMMM = "dd MMM"
            val DMMM = "d MMM"
            val DDMMMYYYY = "dd MMM, yyyy"
            val DDMM = "dd/MM"
            val KK_MM = "kk:mm"
            val HH_MM = "hh:mm a"
            val ATTENDANCE_DATE_FORMAT = "MM/dd/yyyy kk:mm:s a"
            val ATTENDANCE_DATE_FORMAT_1 = "MM/dd/yyyy hh:mm:ss a"
            val CONFERENCE_DATE_FORMAT = "yyyy/MM/dd"
        }
    }

    interface DashBoardFragments {
        companion object {
            val MY_SELF = "MY_SELF"
            val MY_TEAM = "MY_TEAM"
            val NOTIFICATIONS = "NOTIFICATIONS"
        }
    }


    interface Events {
        companion object {
            val NO_INTERNET_CONNECTION = 0
            val LOGIN_SUCCESSFUL = 1
            val LOGIN_UN_SUCCESSFUL = 2
            val WRONG_LOGIN_CREDENTIALS_USED = 3
            val YOU_ARE_USING_OLDER_VERSION_OF_APP = 4
            val YOU_ARE_USING_CURRENT_VERSION_OF_APP = 5
            val OOPS_MESSAGE = 6
            val INTERNAL_ERROR = 500
            val ORDER_PROCESSING = 7
            val PURCHASE = 8
            val LOGISTICS = 9
            val INSTALLATION = 10
            val COLLECTION = 11
            val OTHERS = 12
            val WS = 13
            val NOT_FOUND = 14

            val GET_ORDERPROCESING_REFRESH_SUCCESSFULL = 30
            val GET_ORDERPROCESING_REFRESH_UNSUCCESSFULL = 31
            val GET_ORDERPROCESING_FINANCE_APPROVAL_SUCCESSFULL = 32
            val GET_ORDERPROCESING_FINANCE_APPROVAL_UNSUCCESSFULL = 33
            val GET_ORDERPROCESING_SOAUTHORIZATION_SUCCESSFULL = 34
            val GET_ORDERPROCESING_SOAUTHORIZATION_UNSUCCESSFULL = 35
            val GET_ORDERPROCESING_SPCSUBMISSION_SUCCESSFULL = 36
            val GET_ORDERPROCESING_SPCSUBMISSION_UNSUCCESSFULL = 37
            val GET_ORDERPROCESING_LOWMARGIN_SUCCESSFULL = 38
            val GET_ORDERPROCESING_LOWMARGIN_UNSUCCESSFULL = 39

            val GET_PURCHASE_REFRESH_SUCCESSFULL = 40
            val GET_PURCHASE_REFRESH_UNSUCCESSFULL = 41
            val GET_PURCHASE_SALES_ORDER_SUCCESSFULL = 42
            val GET_PURCHASE_SALES_ORDER_UNSUCCESSFULL = 43
            val GET_PURCHASE_BILLING_SUCCESSFULL = 44
            val GET_PURCHASE_BILLING_UNSUCCESSFULL = 45
            val GET_PURCHASE_STOCK_SUCCESSFULL = 46
            val GET_PURCHASE_STOCK_UNSUCCESSFULL = 47
            val GET_PURCHASE_EDD_SUCCESSFULL = 48
            val GET_PURCHASE_EDD_UNSUCCESSFULL = 49

            val GET_LOGISTICS_REFRESH_SUCCESSFULL = 50
            val GET_LOGISTICS_REFRESH_UNSUCCESSFULL = 51
            val GET_LOGISTICS_DISPATCH_SUCCESSFULL = 52
            val GET_LOGISTICS_DISPATCH_UNSUCCESSFULL = 53
            val GET_LOGISTICS_INTRANSIT_SUCCESSFULL = 54
            val GET_LOGISTICS_INTRANSIT_UNSUCCESSFULL = 55
            val GET_LOGISTICS_HOLD_DELIVERY_SUCCESSFULL = 56
            val GET_LOGISTICS_HOLD_DELIVERY_UNSUCCESSFULL = 57
            val GET_LOGISTICS_ACKNOWLEDGEMENT_SUCCESSFULL = 58
            val GET_LOGISTICS_ACKNOWLEDGEMENT_UNSUCCESSFULL = 59

            val GET_INSTALLATION_REFRESH_SUCCESSFULL = 60
            val GET_INSTALLATION_REFRESH_UNSUCCESSFULL = 61
            val GET_INSTALLATION_OPEN_CALLS_SUCCESSFULL = 62
            val GET_INSTALLATION_OPEN_CALLS_UNSUCCESSFULL = 63
            val GET_INSTALLATION_WIP_SUCCESSFULL = 64
            val GET_INSTALLATION_WIP_UNSUCCESSFULL = 65
            val GET_INSTALLATION_DOA_IR_SUCCESSFULL = 66
            val GET_INSTALLATION_DOA_IR_UNSUCCESSFULL = 67
            val GET_INSTALLATION_HOLD_SUCCESSFULL = 68
            val GET_INSTALLATION_HOLD_UNSUCCESSFULL = 69

            val GET_COLLECTION_REFRESH_SUCCESSFULL = 70
            val GET_COLLECTION_REFRESH_UNSUCCESSFULL = 71
            val GET_COLLECTION_OUTSTANDING_SUCCESSFULL = 72
            val GET_COLLECTION_OUTSTANDING_UNSUCCESSFULL = 73
            val GET_COLLECTION_COLLECTION_SUCCESSFULL = 74
            val GET_COLLECTION_COLLECTION_UNSUCCESSFULL = 75
            val GET_COLLECTION_OS_AGEING_SUCCESSFULL = 76
            val GET_COLLECTION_OS_AGEING_UNSUCCESSFULL = 77
            val GET_COLLECTION_DELIVERY_INSTALLATION_SUCCESSFULL = 78
            val GET_COLLECTION_DELIVERY_INSTALLATION_UNSUCCESSFULL = 79

            val GET_SALES_REFRESH_SUCCESSFULL = 80
            val GET_SALES_REFRESH_UNSUCCESSFULL = 81
            val GET_RECEIVABLE_REFRESH_SUCCESSFULL = 82
            val GET_RECEIVABLE_REFRESH_UNSUCCESSFULL = 83
            val GET_SALES_RECEIVABLE_SALES_SUCCESSFULL = 84
            val GET_SALES_RECEIVABLE_SALES_UNSUCCESSFULL = 85
            val GET_SALES_RECEIVABLE_OUTSTANDING_SUCCESSFULL = 86
            val GET_SALES_RECEIVABLE_OUTSTANDING_UNSUCCESSFULL = 87

            val GET_SALES_RECEIVABLE_SUCCESSFULL = 88
            val GET_SALES_RECEIVABLE_UNSUCCESSFULL = 89
            val GET_FULL_SALES_LIST_SUCCESSFULL = 90
            val GET_FULL_SALES_LIST_UNSUCCESSFULL = 91
            val GET_YTDQTD_SUCCESSFULL = 92
            val GET_YTDQTD_UNSUCCESSFULL = 93
            val GET_SALES_PERSON_LIST_SUCCESSFULL = 94
            val GET_SALES_PERSON_LIST_UNSUCCESSFULL = 95
            val GET_FULL_CUSTOMER_LIST_SUCCESSFULL = 96
            val GET_FULL_CUSTOMER_LIST_UNSUCCESSFULL = 97
            val GET_SELECTED_CUSTOMER_LIST_SUCCESSFULL = 98
            val GET_SELECTED_CUSTOMER_LIST_UNSUCCESSFULL = 99
            val GET_FULL_PRODUCT_LIST_SUCCESSFULL = 100
            val GET_FULL_PRODUCT_LIST_UNSUCCESSFULL = 101
            val GET_SELECTED_PRODUCT_LIST_SUCCESSFULL = 102
            val GET_SELECTED_PRODUCT_LIST_UNSUCCESSFULL = 103
            val GET_FILTER_SALES_LIST_SUCCESSFULL = 104
            val GET_FILTER_SALES_LIST_UNSUCCESSFULL = 105
            val GET_OPEN_SALES_ORDER_LIST_SUCCESSFULL = 106
            val GET_OPEN_SALES_ORDER_LIST_UNSUCCESSFULL = 107
            val GET_OUTSTANDING_LIST_SUCCESSFULL = 108
            val GET_OUTSTANDING_LIST_UNSUCCESSFULL = 109

            val GET_RSM_LIST_SUCCESSFULL = 110
            val GET_RSM_LIST_UNSUCCESSFULL = 111
            val GET_SALES_LIST_SUCCESSFULL = 112
            val GET_SALES_LIST_UNSUCCESSFULL = 113
            val GET_CUSTOMER_LIST_SUCCESSFULL = 114
            val GET_CUSTOMER_LIST_UNSUCCESSFULL = 115
            val GET_PRODUCT_LIST_SUCCESSFULL = 116
            val GET_PRODUCT_LIST_UNSUCCESSFULL = 117

            val GET_RSM_OSO_LIST_SUCCESSFULL = 118
            val GET_RSM_OSO_LIST_UNSUCCESSFULL = 119
            val GET_SALES_OSO_LIST_SUCCESSFULL = 120
            val GET_SALES_OSO_LIST_UNSUCCESSFULL = 121
            val GET_CUSTOMER_OSO_LIST_SUCCESSFULL = 122
            val GET_CUSTOMER_OSO_LIST_UNSUCCESSFULL = 123
            val GET_INVOICE_OSO_LIST_SUCCESSFULL = 124
            val GET_INVOICE_OSO_LIST_UNSUCCESSFULL = 125
            val GET_PRODUCT_OSO_LIST_SUCCESSFULL = 126
            val GET_PRODUCT_OSO_LIST_UNSUCCESSFULL = 127

            val GET_RSM_TOS_LIST_SUCCESSFULL = 128
            val GET_RSM_TOS_LIST_UNSUCCESSFULL = 129
            val GET_SALES_TOS_LIST_SUCCESSFULL = 130
            val GET_SALES_TOS_LIST_UNSUCCESSFULL = 131
            val GET_CUSTOMER_TOS_LIST_SUCCESSFULL = 132
            val GET_CUSTOMER_TOS_LIST_UNSUCCESSFULL = 133
            val GET_PRODUCT_TOS_LIST_SUCCESSFULL = 134
            val GET_PRODUCT_TOS_LIST_UNSUCCESSFULL = 135
            val GET_INVOICE_TOS_LIST_SUCCESSFULL = 136
            val GET_INVOICE_TOS_LIST_UNSUCCESSFULL = 137

            val GET_FISCAL_YEAR_LIST_SUCCESSFULL = 138
            val GET_FISCAL_YEAR_LIST_UNSUCCESSFULL = 139
            val GET_SALES_RECEIVABLE_FISCAL_SUCCESSFULL = 140
            val GET_SALES_RECEIVABLE_FISCAL_UNSUCCESSFULL = 141

            val GET_INVOICE_LOAD_MORE_SUCCESSFULL = 142
            val GET_INVOICE_LOAD_MORE_UNSUCCESSFULL = 143

            val GET_INVOICE_SERACH_SUCCESSFULL = 144
            val GET_INVOICE_SERACH_UNSUCCESSFULL = 145

            val GET_SO_LOAD_MORE_SUCCESSFULL = 146
            val GET_SO_LOAD_MORE_UNSUCCESSFULL = 147

            val GET_SO_SERACH_SUCCESSFULL = 148
            val GET_SO_SERACH_UNSUCCESSFULL = 149

            val GET_ACTIVE_EMPLOYEE_ACCESS_SUCCESSFUL = 150
            val GET_ACTIVE_EMPLOYEE_ACCESS_UNSUCCESSFUL = 151

            val GET_SAVE_SESSION_DETAIL_SUCCESSFUL = 152
            val GET_SAVE_SESSION_DETAIL_UNSUCCESSFUL = 153

            val ITEM_SELECTED = 154
            val ITEM_UNSELECTED = 155
        }
    }

    interface ToastTexts {
        companion object {
            val LOGIN_SUCCESSFULL = "Login Successfull.."
            val NO_INTERNET_CONNECTION = "No Internet Connection..."
            val NO_RECORD_FOUND = "No Record Found..."
            val WRONG_LOGIN_CREDENTIALS_USED = "Wrong Login Credential..."
            val LOGIN_UNSUCCESSFULL = "Login Unsuccessfull..."
            val OOPS_MESSAGE = "Oops Something went wrong..."
            val WORK_PROGRESS = "Work is in progress..."
            val CANCEL = "CANCEL"
            val UPDATE = "UPDATE"
            val YOU_ARE_USING_OLDER_VERSION_OF_TEAM_WORKS_APP_PLEASE_USE_LATEST_VERSION = "You are using older version of TeamWorks" + " app please use latest version."
            val INFORMATION = "Information"
        }
    }

    interface Fragments {
        companion object {
            val NONE = 0
            val USER_PROFILE_FRAGMENTS = 101
            val EDIT_PROFILE_FRAGMENTS = 102
            val HOME_FRAGMENTS = 103
            val ORDERPROCESSING_FRAGMENTS = 104
            val ORDERPROCESSING_FRAGMENTS1 = 1041
            val ORDERPROCESSING_FRAGMENTS2 = 1042
            val ORDERPROCESSING_FRAGMENTS3 = 1043
            val ORDERPROCESSING_FRAGMENTS4 = 1044
            val PURCHASE_FRAGMENTS = 105
            val PURCHASE_FRAGMENTS1 = 1051
            val PURCHASE_FRAGMENTS2 = 1052
            val PURCHASE_FRAGMENTS3 = 1053
            val PURCHASE_FRAGMENTS4 = 1054
            val LOGISTICS_FRAGMENTS = 106
            val LOGISTICS_FRAGMENTS1 = 1061
            val LOGISTICS_FRAGMENTS2 = 1062
            val LOGISTICS_FRAGMENTS3 = 1063
            val LOGISTICS_FRAGMENTS4 = 1064
            val INSTALLATION_FRAGMENTS = 107
            val INSTALLATION_FRAGMENTS1 = 1071
            val INSTALLATION_FRAGMENTS2 = 1072
            val INSTALLATION_FRAGMENTS3 = 1073
            val INSTALLATION_FRAGMENTS4 = 1074
            val COLLECTION_FRAGMENTS = 108
            val COLLECTION_FRAGMENTS1 = 1081
            val COLLECTION_FRAGMENTS2 = 1082
            val COLLECTION_FRAGMENTS3 = 1083
            val COLLECTION_FRAGMENTS4 = 1084
            val OTHERS_FRAGMENTS = 109
            val HELP_FRAGMENTS = 110
            val FEEDBACK_FRAGMENTS = 111
            val SR_FRAGMENTS = 112
            val SR_FRAGMENTS1 = 1121
            val SR_FRAGMENTS2 = 1122

            val RSM_ANALYSIS_FRAGMENT = 113
            val ACCOUNT_FRAGMENT = 114
            val CUSTOMER_FRAGMENT = 115
            val PRODUCT_FRAGMENT = 116

            val SALES_ANALYSIS_FRAGMENT = 117
            val CUSTOMER_ANALYSIS_FRAGMENT = 118

            val WS_RSM_FRAGMENT = 1001
            val WS_ACCOUNT_FRAGMENT = 1002
            val WS_CUSTOMER_FRAGMENT = 1003
            val WS_PRODUCT_FRAGMENT = 1004

            val OSO_RSM_FRAGMENT = 1005
            val OSO_ACCOUNT_FRAGMENT = 1006
            val OSO_CUSTOMER_FRAGMENT = 1007
            val OSO_INVOICE_FRAGMENT = 1008

            val TOS_RSM_FRAGMENT = 1009
            val TOS_ACCOUNT_FRAGMENT = 1010
            val TOS_CUSTOMER_FRAGMENT = 1011
            val TOS_PRODUCT_FRAGMENT = 1012
        }
    }

    interface ClickEvents {
        companion object {
            val RSM_ITEM = 201
            val ACCOUNT_ITEM = 202
            val CUSTOMER_ITEM = 203
            val STATE_ITEM = 204
            val RSM_CLICK = 205
            val SP_CLICK = 206
            val CUSTOMER_SELECT = 207
            val STATE_SELECT = 208
            val RSM_MENU_SELECT = 209
            val CUSTOMER_MENU_SELECT = 210
            val SP_MENU_SELECT = 211
            val PRODUCT_MENU_SELECT = 212

            val WS_RSM_SEARCH = 213
            val WS_SP_SEARCH = 214
            val WS_CUSTOMER_SEARCH = 215
            val WS_PRODUCT_SEARCH = 216

            val OSO_RSM_SEARCH = 217
            val OSO_SP_SEARCH = 218
            val OSO_CUSTOMER_SEARCH = 219
            val OSO_INVOICE_SEARCH = 220

            val TOS_RSM_SEARCH = 221
            val TOS_SP_SEARCH = 212
            val TOS_CUSTOMER_SEARCH = 223
            val TOS_PRODUCT_SEARCH = 224
        }
    }

    interface BackpressEvents {
        companion object {
            val R1_BACK_PRESS = 301
            val R2_BACK_PRESS = 302
            val R3_BACK_PRESS = 303
            val R4_BACK_PRESS = 304
        }
        /*int CUSTOMER_BACK_PRESS = 305;
        int CUSTOMER_BACK_PRESS = 306;
        int CUSTOMER_BACK_PRESS = 307;
        int CUSTOMER_BACK_PRESS = 308;*/
    }
}