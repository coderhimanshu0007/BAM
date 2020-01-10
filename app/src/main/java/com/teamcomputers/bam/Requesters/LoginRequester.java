package com.teamcomputers.bam.Requesters;

import android.util.Log;
import android.widget.Toast;

import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.UserProfile;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.controllers.SharedPreferencesController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

//import com.teamcomputers.teamworks.database.tables.UserProfileTable;

public class LoginRequester implements BaseRequester {

    private String tmcId;
    private String password;

    public LoginRequester(String tmcId, String password) {
        this.tmcId = tmcId;
        this.password = password;
    }

    @Override
    public void run() {
        ApiResponse<LoginModel> apiResponse = HTTPOperationController.loginUser(tmcId, password);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //EventBus.getDefault().post(new EventObject(Events.USER_LOGIN_SUCCESSFUL, null));
                if (apiResponse.getResponse() != null) {
                    LoginModel loginModel = (LoginModel) apiResponse.getResponse();
                    //Log.e("Name", loginModel.getFirstName());
                    //if (apiResponse.getResponse().getStatus()) {
                        /*EventBus.getDefault().post(new EventObject(Events.USER_PASSWORD_IS_NOT_UPDATED, apiResponse.getResponse()));
                        SharedPreferencesController.getInstance(BAMApplication.getInstance()).clear();
                        SharedPreferencesController.getInstance(BAMApplication.getInstance()).clear();
                    } else {*/
                    SharedPreferencesController.getInstance(BAMApplication.getInstance()).setUserLoggedIn(true);
                    SharedPreferencesController.getInstance(BAMApplication.getInstance()).setUserProfile(apiResponse.getResponse());
                    EventBus.getDefault().post(new EventObject(Events.LOGIN_SUCCESSFUL, null));
//                                    UserProfileTable.getUserProfileTable().insert(apiResponse.getResponse());

//                        ApiResponse<UserProfile> reportingHeadProfile = HTTPOperationController.getUserDetails(apiResponse.getResponse().getAuthToken(), apiResponse.getResponse().getReportingHeadId());
//                        if (reportingHeadProfile != null) {
//                            if (reportingHeadProfile.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                                if (reportingHeadProfile.getResponse() != null) {
//                                    apiResponse.getResponse().setReportingHeadName(reportingHeadProfile.getResponse().getMemberName());
//
//                                    SharedPreferencesController.getInstance(TeamWorksApplication.getInstance()).setUserLoggedIn(true);
//                                    SharedPreferencesController.getInstance(TeamWorksApplication.getInstance()).setUserProfile(apiResponse.getResponse());
////                                    UserProfileTable.getUserProfileTable().insert(apiResponse.getResponse());
//
//                                    EventBus.getDefault().post(new EventObject(Events.USER_LOGIN_SUCCESSFUL, null));
//                                } else {
//                                    EventBus.getDefault().post(new EventObject(Events.USER_LOGIN_UN_SUCCESSFUL, null));
//                                }
//                            }
//                        } else {
//                            EventBus.getDefault().post(new EventObject(Events.USER_LOGIN_UN_SUCCESSFUL, null));
//                        }
                    // }
                } else {
                    EventBus.getDefault().post(new EventObject(Events.LOGIN_UN_SUCCESSFUL, null));
                }
            } else  if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                EventBus.getDefault().post(new EventObject(Events.WRONG_LOGIN_CREDENTIALS_USED, null));
            }
        } else {
            EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
        }
    }
}
