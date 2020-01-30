package com.teamcomputers.bam.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.FAModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.LowMarginModel;
import com.teamcomputers.bam.Models.SOAModel;
import com.teamcomputers.bam.Models.SPCSModel;
import com.teamcomputers.bam.Models.SalesDataModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.Utils.BAMUtil;

//Controller for storing and retrieving data from Shared Preferences
public class SharedPreferencesController implements BAMConstant {

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

    public LoginModel getUserProfile() {
        String json = getString(Keys.USER_PROFILE.getLabel(), "");
        return (LoginModel) BAMUtil.fromJson(json, LoginModel.class);
    }

    public void setUserProfile(LoginModel userProfile) {
        putString(Keys.USER_PROFILE.getLabel(), BAMUtil.toJson(userProfile));
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

    public FAModel[] getOPFAData() {
        String json = getString(Keys.OPFA_DATA.getLabel(), null);
        return (FAModel[]) BAMUtil.fromJson(json, FAModel[].class);
    }

    public void setOPFAData(FAModel[] opfaData) {
        putString(Keys.OPFA_DATA.getLabel(), BAMUtil.toJson(opfaData));
    }

    public SOAModel[] getOPSOAData() {
        String json = getString(Keys.OPSOA_DATA.getLabel(), null);
        return (SOAModel[]) BAMUtil.fromJson(json, SOAModel[].class);
    }

    public void setOPSOAData(SOAModel[] opfaData) {
        putString(Keys.OPSOA_DATA.getLabel(), BAMUtil.toJson(opfaData));
    }

    public SPCSModel[] getSPCSData() {
        String json = getString(Keys.SPCS_DATA.getLabel(), null);
        return (SPCSModel[]) BAMUtil.fromJson(json, SPCSModel[].class);
    }

    public void setSPCSData(SPCSModel[] opfaData) {
        putString(Keys.SPCS_DATA.getLabel(), BAMUtil.toJson(opfaData));
    }

    public LowMarginModel[] getLMData() {
        String json = getString(Keys.LM_DATA.getLabel(), null);
        return (LowMarginModel[]) BAMUtil.fromJson(json, LowMarginModel[].class);
    }

    public void setLMData(LowMarginModel[] opfaData) {
        putString(Keys.LM_DATA.getLabel(), BAMUtil.toJson(opfaData));
    }

    public void setSalesData(SalesDataModel[] opfaData) {
        putString(Keys.SALES_DATA.getLabel(), BAMUtil.toJson(opfaData));
    }

    public SalesDataModel[] getSalesData() {
        String json = getString(Keys.SALES_DATA.getLabel(), null);
        return (SalesDataModel[]) BAMUtil.fromJson(json, SalesDataModel[].class);
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

    private enum Keys {
        USER_PROFILE("USER_PROFILE"),
        LOGGED_IN("LOGGED_IN"),
        ORDERPROCESSING_PAGENO("ORDERPROCESSING_PAGENO"),
        OPFA_DATA("OPFA_DATA"),
        OPSOA_DATA("OPSOA_DATA"),
        SPCS_DATA("SPCS_DATA"),
        LM_DATA("LM_DATA"),
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
