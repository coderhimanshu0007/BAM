package com.teamcomputers.bam.Fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<LoginModel> userData;
    //private MutableLiveData<String> nameText;

    public HomeViewModel() {
        userData = new MutableLiveData<>();
        LoginModel user = new LoginModel();
        user = SharedPreferencesController.getInstance(BAMApplication.getInstance()).getUserProfile();
        //User user = new User();
        //user.setHelloText("Hello,");
        //user.setNameText("Manoj Kumar");
        //userData.setValue(user);
        userData.setValue(user);
    }

    public LiveData<LoginModel> getText() {
        return userData;
    }

    public class User {
        String helloText;
        String nameText;

        public String getHelloText() {
            return helloText;
        }

        public void setHelloText(String helloText) {
            this.helloText = helloText;
        }

        public String getNameText() {
            return nameText;
        }

        public void setNameText(String nameText) {
            this.nameText = nameText;
        }
    }
}