package com.teamcomputers.bam.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.teamcomputers.bam.Interface.KBAMConstant;
import com.teamcomputers.bam.Models.AcknowledgemantModel;
import com.teamcomputers.bam.Models.ActiveEmployeeAccessModel;
import com.teamcomputers.bam.Models.DOAIRModel;
import com.teamcomputers.bam.Models.DispatchModel;
import com.teamcomputers.bam.Models.FAModel;
import com.teamcomputers.bam.Models.HoldDeliveryModel;
import com.teamcomputers.bam.Models.HoldModel;
import com.teamcomputers.bam.Models.InTransitModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.LowMarginModel;
import com.teamcomputers.bam.Models.OpenCallsModel;
import com.teamcomputers.bam.Models.SOAModel;
import com.teamcomputers.bam.Models.SPCSModel;
import com.teamcomputers.bam.Models.SalesDataModel;
import com.teamcomputers.bam.Models.SessionDataModel;
import com.teamcomputers.bam.Models.SessionDetailsModel;
import com.teamcomputers.bam.Models.WIPModel;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

//Controller for storing and retrieving data from Shared Preferences
public class SharedPreferencesController implements KBAMConstant {

    public static final String TAG = SharedPreferencesController.class.getName();
    public static final String SHARED_PREF_NAME = "team_work_preferences";
    private static SharedPreferencesController sharedPreferencesController;
    private SharedPreferences sharedPreferences;

    private SharedPreferencesController() {
    }

    public static SharedPreferencesController getInstance(Context context) {
        if (sharedPreferencesController == null) {
            sharedPreferencesController = new SharedPreferencesController();

        }
        if (sharedPreferencesController.sharedPreferences == null) {
            sharedPreferencesController.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferencesController;
    }

    /**
     * This Method Clear shared preference.
     */
    public void clear() {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private boolean getBoolean(String key, boolean deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, deafultValue);
        }

        return deafultValue;
    }

    private void putBoolean(String key, boolean value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putBoolean(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put Boolean in Shared preference", e);
        }
    }

    private String getString(String key, String deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, deafultValue);
        }

        return deafultValue;
    }

    private void putString(String key, String value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put String in Shared preference", e);
        }
    }

    private long getLong(String key, long deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, deafultValue);
        }

        return deafultValue;
    }

    private void putLong(String key, long value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putLong(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put Boolean in Shared preference", e);
        }
    }

    private int getInt(String key, Integer deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, deafultValue);
        }
        return deafultValue;
    }

    private void putInt(String key, Integer value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putInt(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put Integer in Shared preference", e);
        }
    }

    /*private String getEventObject(String key, String deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, deafultValue);
        }
        return deafultValue;
    }

    private void putEventObject(String key, EventObject value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putString(key, String.valueOf(value));
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put Integer in Shared preference", e);
        }
    }*/

    public void setUserId(String userId) {
        putString(Keys.USERID.getLabel(), BAMUtil.toJson(userId));
    }

    public String getUserId() {
        return getString(Keys.USERID.getLabel(), "");
    }

    public LoginModel getUserProfile() {
        String json = getString(Keys.USER_PROFILE.getLabel(), "");
        return (LoginModel) BAMUtil.fromJson(json, LoginModel.class);
    }

    public void setUserProfile(LoginModel userProfile) {
        putString(Keys.USER_PROFILE.getLabel(), BAMUtil.toJson(userProfile));
    }

    public ActiveEmployeeAccessModel getActiveEmployeeAccess() {
        String json = getString(Keys.ACTIVE_EMPLOYEE_ACCESS.getLabel(), "");
        return (ActiveEmployeeAccessModel) BAMUtil.fromJson(json, ActiveEmployeeAccessModel.class);
    }

    public void setActiveEmployeeAccess(ActiveEmployeeAccessModel activeEmployeeAccess) {
        putString(Keys.ACTIVE_EMPLOYEE_ACCESS.getLabel(), BAMUtil.toJson(activeEmployeeAccess));
    }

    public SessionDetailsModel getSessionDetail() {
        String json = getString(Keys.SESSION_DETAIL.getLabel(), "");
        return (SessionDetailsModel) BAMUtil.fromJson(json, SessionDetailsModel.class);
    }

    public void setSessionDetail(SessionDetailsModel activeEmployeeAccess) {
        putString(Keys.SESSION_DETAIL.getLabel(), BAMUtil.toJson(activeEmployeeAccess));
    }

    public SessionDataModel getSessionData() {
        String json = getString(Keys.SESSION_DATA.getLabel(), "");
        return (SessionDataModel) BAMUtil.fromJson(json, SessionDataModel.class);
    }

    public void setSessionData(SessionDataModel activeEmployeeAccess) {
        putString(Keys.SESSION_DATA.getLabel(), BAMUtil.toJson(activeEmployeeAccess));
    }

    public boolean isUserLoggedIn() {
        return getBoolean(Keys.LOGGED_IN.getLabel(), false);
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        putBoolean(Keys.LOGGED_IN.getLabel(), userLoggedIn);
    }

    public int getOPPageNo() {
        return getInt(Keys.ORDERPROCESSING_PAGENO.getLabel(), 0);
    }

    public void setOPPageNo(Integer pageNo) {
        putInt(Keys.ORDERPROCESSING_PAGENO.getLabel(), pageNo);
    }

    public int getSharing() {
        return getInt(Keys.SHARING.getLabel(), 0);
    }

    public void setSharing(Integer pageNo) {
        putInt(Keys.SHARING.getLabel(), pageNo);
    }

    public FAModel[] getOPFAData() {
        String json = getString(Keys.OPFA_DATA.getLabel(), null);
        return (FAModel[]) BAMUtil.fromJson(json, FAModel[].class);
    }

    public void setOPFAData(FAModel[] faData) {
        putString(Keys.OPFA_DATA.getLabel(), BAMUtil.toJson(faData));
    }

    public SOAModel[] getOPSOAData() {
        String json = getString(Keys.OPSOA_DATA.getLabel(), null);
        return (SOAModel[]) BAMUtil.fromJson(json, SOAModel[].class);
    }

    public void setOPSOAData(SOAModel[] soaData) {
        putString(Keys.OPSOA_DATA.getLabel(), BAMUtil.toJson(soaData));
    }

    public SPCSModel[] getSPCSData() {
        String json = getString(Keys.SPCS_DATA.getLabel(), null);
        return (SPCSModel[]) BAMUtil.fromJson(json, SPCSModel[].class);
    }

    public void setSPCSData(SPCSModel[] spcsData) {
        putString(Keys.SPCS_DATA.getLabel(), BAMUtil.toJson(spcsData));
    }

    public LowMarginModel[] getLMData() {
        String json = getString(Keys.LM_DATA.getLabel(), null);
        return (LowMarginModel[]) BAMUtil.fromJson(json, LowMarginModel[].class);
    }

    public void setLMData(LowMarginModel[] opfaData) {
        putString(Keys.LM_DATA.getLabel(), BAMUtil.toJson(opfaData));
    }

    public void setSalesData(SalesDataModel[] salesData) {
        putString(Keys.SALES_DATA.getLabel(), BAMUtil.toJson(salesData));
    }

    public SalesDataModel[] getSalesData() {
        String json = getString(Keys.SALES_DATA.getLabel(), null);
        return (SalesDataModel[]) BAMUtil.fromJson(json, SalesDataModel[].class);
    }

    public void setDispatchData(DispatchModel[] dispatchData) {
        putString(Keys.DISPATCH_DATA.getLabel(), BAMUtil.toJson(dispatchData));
    }

    public DispatchModel[] getDispatchData() {
        String json = getString(Keys.DISPATCH_DATA.getLabel(), null);
        return (DispatchModel[]) BAMUtil.fromJson(json, DispatchModel[].class);
    }

    public void setInTransitData(InTransitModel[] inTransitData) {
        putString(Keys.INTRANSIT_DATA.getLabel(), BAMUtil.toJson(inTransitData));
    }

    public InTransitModel[] getInTransitData() {
        String json = getString(Keys.INTRANSIT_DATA.getLabel(), null);
        return (InTransitModel[]) BAMUtil.fromJson(json, InTransitModel[].class);
    }

    public void setHoldDeliveryData(HoldDeliveryModel[] holdDeliveryData) {
        putString(Keys.HOLDDELIVERY_DATA.getLabel(), BAMUtil.toJson(holdDeliveryData));
    }

    public HoldDeliveryModel[] getHoldDeliveryData() {
        String json = getString(Keys.HOLDDELIVERY_DATA.getLabel(), null);
        return (HoldDeliveryModel[]) BAMUtil.fromJson(json, HoldDeliveryModel[].class);
    }

    public void setAcknowledgementData(AcknowledgemantModel[] ackData) {
        putString(Keys.ACKNOWLEDGEMENT_DATA.getLabel(), BAMUtil.toJson(ackData));
    }

    public AcknowledgemantModel[] getAcknowledgementData() {
        String json = getString(Keys.ACKNOWLEDGEMENT_DATA.getLabel(), null);
        return (AcknowledgemantModel[]) BAMUtil.fromJson(json, AcknowledgemantModel[].class);
    }

    public void setOpenCallsData(OpenCallsModel[] openCallsData) {
        putString(Keys.OPENCALL_DATA.getLabel(), BAMUtil.toJson(openCallsData));
    }

    public OpenCallsModel[] getOpenCallsData() {
        String json = getString(Keys.OPENCALL_DATA.getLabel(), null);
        return (OpenCallsModel[]) BAMUtil.fromJson(json, OpenCallsModel[].class);
    }

    public void setWIPData(WIPModel[] wipData) {
        putString(Keys.WIP_DATA.getLabel(), BAMUtil.toJson(wipData));
    }

    public WIPModel[] getWIPData() {
        String json = getString(Keys.WIP_DATA.getLabel(), null);
        return (WIPModel[]) BAMUtil.fromJson(json, WIPModel[].class);
    }

    public void setDOAIRData(DOAIRModel[] doairData) {
        putString(Keys.DOAIR_DATA.getLabel(), BAMUtil.toJson(doairData));
    }

    public DOAIRModel[] getDOAIRData() {
        String json = getString(Keys.DOAIR_DATA.getLabel(), null);
        return (DOAIRModel[]) BAMUtil.fromJson(json, DOAIRModel[].class);
    }

    public void setHoldData(HoldModel[] holdData) {
        putString(Keys.HOLD_DATA.getLabel(), BAMUtil.toJson(holdData));
    }

    public HoldModel[] getHoldData() {
        String json = getString(Keys.HOLD_DATA.getLabel(), null);
        return (HoldModel[]) BAMUtil.fromJson(json, HoldModel[].class);
    }

    public int getPurchasePageNo() {
        return getInt(Keys.PURCHASE_PAGENO.getLabel(), 0);
    }

    public void setPurchasePageNo(Integer pageNo) {
        putInt(Keys.PURCHASE_PAGENO.getLabel(), pageNo);
    }

    public int getLogisticPageNo() {
        return getInt(Keys.LOGISTICS_PAGENO.getLabel(), 0);
    }

    public void setLogisticPageNo(Integer pageNo) {
        putInt(Keys.LOGISTICS_PAGENO.getLabel(), pageNo);
    }

    public int getInstallationPageNo() {
        return getInt(Keys.INSTALLATION_PAGENO.getLabel(), 0);
    }

    public void setInstallationPageNo(Integer pageNo) {
        putInt(Keys.INSTALLATION_PAGENO.getLabel(), pageNo);
    }

    public int getCollectionPageNo() {
        return getInt(Keys.COLLECTION_PAGENO.getLabel(), 0);
    }

    public void setCollectionPageNo(Integer pageNo) {
        putInt(Keys.COLLECTION_PAGENO.getLabel(), pageNo);
    }

    public int getSalesReceivablePageNo() {
        return getInt(Keys.SALES_PAGENO.getLabel(), 0);
    }

    public void setSalesReceivablePageNo(Integer pageNo) {
        putInt(Keys.SALES_PAGENO.getLabel(), pageNo);
    }

    @Override
    public int getDELAY_MILLIS() {
        return 2000;
    }

    @NotNull
    @Override
    public Pattern getEMAIL_VALIDATION_PATTERN() {
        Pattern compile = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        return compile;
    }


    private enum Keys {
        USERID("USERID"),
        USER_PROFILE("USER_PROFILE"),
        ACTIVE_EMPLOYEE_ACCESS("ACTIVE_EMPLOYEE_ACCESS"),
        SESSION_DETAIL("SESSION_DETAIL"),
        SESSION_DATA("SESSION_DATA"),
        LOGGED_IN("LOGGED_IN"),
        ORDERPROCESSING_PAGENO("ORDERPROCESSING_PAGENO"),
        SHARING("SHARING"),
        OPFA_DATA("OPFA_DATA"),
        OPSOA_DATA("OPSOA_DATA"),
        SPCS_DATA("SPCS_DATA"),
        LM_DATA("LM_DATA"),
        DISPATCH_DATA("DISPATCH_DATA"),
        INTRANSIT_DATA("INTRANSIT_DATA"),
        HOLDDELIVERY_DATA("HOLDDELIVERY_DATA"),
        ACKNOWLEDGEMENT_DATA("ACKNOWLEDGEMENT_DATA"),
        OPENCALL_DATA("OPENCALL_DATA"),
        WIP_DATA("WIP_DATA"),
        DOAIR_DATA("DOAIR_DATA"),
        HOLD_DATA("HOLD_DATA"),
        SALES_DATA("SALES_DATA"),
        PURCHASE_PAGENO("PURCHASE_PAGENO"),
        LOGISTICS_PAGENO("LOGISTICS_PAGENO"),
        INSTALLATION_PAGENO("INSTALLATION_PAGENO"),
        COLLECTION_PAGENO("COLLECTION_PAGENO"),
        SALES_PAGENO("SALES_PAGENO");
        private String label;

        Keys(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }


}
