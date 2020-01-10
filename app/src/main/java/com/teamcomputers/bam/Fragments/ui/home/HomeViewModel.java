package com.teamcomputers.bam.Fragments.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<User> userData;
    //private MutableLiveData<String> nameText;

    public HomeViewModel() {
        userData = new MutableLiveData<>();
        User user = new User();
        user.setHelloText("Hello,");
        user.setNameText("Manoj Kumar");
        userData.setValue(user);
    }

    public LiveData<User> getText() {
        return userData;
    }

    public class User{
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