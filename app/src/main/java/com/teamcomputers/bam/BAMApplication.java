package com.teamcomputers.bam;

import android.app.Application;
import android.content.res.TypedArray;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Interface.BaseManagerInterface;
import com.teamcomputers.bam.Interface.OnLoadListener;
import com.teamcomputers.bam.Interface.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//Main Entry Point for app also app Specifics things must be initialized here
public class BAMApplication extends Application implements BAMConstant {

    private static String TAG = BAMApplication.class.getName();
    private static BAMApplication bamApplication;
    private ApiInterface apiInterface;
    private ApiInterface apiInterface2;
    private boolean closed;
    private ArrayList registeredManagers;
    private Map<Class<? extends BaseManagerInterface>, Collection<? extends BaseManagerInterface>> managerInterfaces;
    private ArrayList<Object> holidayObject = new ArrayList<>();
    private FirebaseRemoteConfig remoteConfig;

    public BAMApplication() {
        bamApplication = this;
        closed = false;
        registeredManagers = new ArrayList<>();
        managerInterfaces = new HashMap<>();
    }

    public static BAMApplication getInstance() {
        if (bamApplication == null) {
            bamApplication = new BAMApplication();
        }
        return bamApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initCrashlytics();
        //initManagers();
//        initDB();
        //loadManagers();
        initRetrofit();
        //initFirebaseRemoteConfig();
    }

    private void initManagers() {
        TypedArray clienttables = getResources().obtainTypedArray(
                R.array.team_work_managers);
        for (int index = 0; index < clienttables.length(); index++) {
            try {
                Class.forName(clienttables.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        clienttables.recycle();
    }

    private void initDB() {
        TypedArray tables = getResources().obtainTypedArray(
                R.array.labtabtables);
        for (int index = 0; index < tables.length(); index++) {
            try {
                Class.forName(tables.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        tables.recycle();
    }

    @SuppressWarnings("unchecked")
    public void addManager(Object manager) {
        registeredManagers.add(manager);
    }

    private void loadManagers() {
        for (OnLoadListener listener : getManagers(OnLoadListener.class)) {
            listener.onLoad();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseManagerInterface> Collection<T> getManagers(
            Class<T> cls) {
        if (closed)
            return Collections.emptyList();
        Collection<T> collection = (Collection<T>) managerInterfaces.get(cls);
        if (collection == null) {
            collection = new ArrayList<>();
            for (Object manager : registeredManagers)
                if (cls.isInstance(manager))
                    collection.add((T) manager);
            collection = Collections.unmodifiableCollection(collection);
            managerInterfaces.put(cls, collection);
        }
        return collection;
    }

    public void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.TEAM_WORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.TEAM_WORK_ECR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiInterface2 = retrofit.create(ApiInterface.class);

    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }

    public ApiInterface getApiInterface2() {
        return apiInterface2;
    }

    public ArrayList<Object> getHolidayObject() {
        return holidayObject;
    }

    public void setHolidayObject(ArrayList<Object> holidayObject) {
        this.holidayObject = holidayObject;
    }

    /*public void initCrashlytics() {
        Fabric.with(this, new Crashlytics());
    }*/


    /*private void initFirebaseRemoteConfig() {
        FirebaseApp.initializeApp(bamApplication);
        remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build()
        );

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(FbRemoteConfig.UPDATE_ANDROID_APP_KEY, FbRemoteConfig.APP_VERSION_DEFAULTS);
        remoteConfig.setDefaults(defaults);
    }*/

    public FirebaseRemoteConfig getRemoteConfig() {
        return remoteConfig;
    }
}
