package com.teamcomputers.bam

import android.app.Application
import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Interface.BaseManagerInterface
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.OnLoadListener
import com.teamcomputers.bam.Interface.retrofit.KApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

open class KBAMApplication : Application(), BAMConstant {
    private val TAG = KBAMApplication::class.java.name
    private var bamApplication: KBAMApplication? = null
    private var apiInterface2: KApiInterface? = null
    private var closed: Boolean = false
    val TEAM_WORK_BASE_URL: String
        get() = "http://bam.teamcomputers.com:5558/api/"
    private lateinit var registeredManagers: ArrayList<*>
    private lateinit var managerInterfaces: MutableMap<Class<out BaseManagerInterface>, Collection<BaseManagerInterface>>
    private var holidayObject = ArrayList<Any>()
    private val remoteConfig: FirebaseRemoteConfig? = null


    companion object {
        private var instance: KBAMApplication? = null
        var ctx: Context? = null
        var apiInterface: KApiInterface? = null

        fun applicationContext(): KBAMApplication {
            return instance as KBAMApplication
        }
    }

    fun KBAMApplication(): KBAMApplication? {
        bamApplication = this
        closed = false
        registeredManagers = ArrayList<Any>()
        managerInterfaces = HashMap()
        return bamApplication
    }

    fun getInstance(): KBAMApplication? {
        if (bamApplication == null) {
            bamApplication = KBAMApplication()
        }
        return bamApplication
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        //initCrashlytics();
        //initManagers();
        //        initDB();
        //loadManagers();
        initRetrofit()
        //initFirebaseRemoteConfig();
    }

    private fun initManagers() {
        val clienttables = resources.obtainTypedArray(
                R.array.team_work_managers)
        for (index in 0 until clienttables.length()) {
            try {
                Class.forName(clienttables.getString(index)!!)
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            }

        }
        clienttables.recycle()
    }

    private fun initDB() {
        val tables = resources.obtainTypedArray(
                R.array.labtabtables)
        for (index in 0 until tables.length()) {
            try {
                Class.forName(tables.getString(index)!!)
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            }

        }
        tables.recycle()
    }

    /*fun addManager(manager: Any) {
        registeredManagers.add(manager)
    }*/

    private fun loadManagers() {
        for (listener in getManagers(OnLoadListener::class.java)) {
            listener.onLoad()
        }
    }

    fun <T : BaseManagerInterface> getManagers(
            cls: Class<T>): Collection<T> {
        if (closed)
            return emptyList()
        var collection: Collection<T>? = managerInterfaces[cls] as Collection<T>?
        if (collection == null) {
            collection = ArrayList()
            for (manager in registeredManagers)
                if (cls.isInstance(manager))
                    collection.add(manager as T)
            collection = Collections.unmodifiableCollection(collection)
            managerInterfaces[cls] = collection!!
        }
        return collection
    }

    fun initRetrofit() {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
        val client = builder.build()

        var retrofit = Retrofit.Builder()
                .baseUrl(TEAM_WORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        apiInterface = retrofit.create(KApiInterface::class.java)

        /*retrofit = Retrofit.Builder()
                .baseUrl(KApiInterface.TEAM_WORK_ECR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        apiInterface2 = retrofit.create(KApiInterface::class.java)
*/
    }

    fun getApiInterface(): KApiInterface? {
        return apiInterface
    }

    fun getApiInterface2(): KApiInterface? {
        return apiInterface2
    }

    fun getHolidayObject(): ArrayList<Any> {
        return holidayObject
    }

    fun setHolidayObject(holidayObject: ArrayList<Any>) {
        this.holidayObject = holidayObject
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

    fun getRemoteConfig(): FirebaseRemoteConfig? {
        return remoteConfig
    }
}