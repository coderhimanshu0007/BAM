package com.teamcomputers.bam.Fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.Unbinder
import com.teamcomputers.bam.Activities.KDashboardActivity
import com.teamcomputers.bam.BAMApplication
import com.teamcomputers.bam.Fragments.KBaseFragment
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.LoginModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.controllers.SharedPreferencesController
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.greenrobot.eventbus.EventBus

class KHomeFragment : KBaseFragment() {
    private var rootView: View? = null
    private var unbinder: Unbinder? = null
    var userProfile: LoginModel? = null
    private var dashboardActivityContext: KDashboardActivity? = null
    private var homeViewModel: HomeViewModel? = null
    internal var toolbarTitle = ""

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        unbinder = ButterKnife.bind(this, rootView!!)
        dashboardActivityContext = context as KDashboardActivity
        toolbarTitle = getString(R.string.Heading_Home)
        dashboardActivityContext!!.setToolBarTitle(toolbarTitle)
        val text_hello = rootView!!.findViewById<TextView>(R.id.text_hello)
        val text_name = rootView!!.findViewById<TextView>(R.id.text_name)
        userProfile = SharedPreferencesController.getInstance(BAMApplication.getInstance()).userProfile
        /*homeViewModel.getText().observe(this, new Observer<HomeViewModel.User>() {
            @Override
            public void onChanged(@Nullable HomeViewModel.User s) {
                text_hello.setText(s.helloText);
                text_name.setText(userProfile.getFirstName());
            }
        });*/
        homeViewModel!!.text.observe(this, Observer { loginModel ->
            //text_hello.setText("Hello,");
            var name: String? = null
            if (loginModel.memberName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 2) {
                name = (loginModel.memberName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                        + " " + loginModel.memberName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[loginModel.memberName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1])
            } else if (loginModel.memberName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size <= 2) {
                name = loginModel.memberName
            }
            text_hello.text = "Hello " + name!!
        })

        rootView?.rlaOrderProcessing?.setOnClickListener(View.OnClickListener { EventBus.getDefault().post(EventObject(KBAMConstant.Events.ORDER_PROCESSING, null)) })
        rootView?.rlaPurchase?.setOnClickListener(View.OnClickListener { EventBus.getDefault().post(EventObject(KBAMConstant.Events.PURCHASE, null)) })
        rootView?.rlaLogistics?.setOnClickListener(View.OnClickListener { EventBus.getDefault().post(EventObject(KBAMConstant.Events.LOGISTICS, null)) })
        rootView?.rlaInstallation?.setOnClickListener(View.OnClickListener { EventBus.getDefault().post(EventObject(KBAMConstant.Events.INSTALLATION, null)) })
        rootView?.rlaCollection?.setOnClickListener(View.OnClickListener { EventBus.getDefault().post(EventObject(KBAMConstant.Events.COLLECTION, null))})
        rootView?.rlaWS?.setOnClickListener(View.OnClickListener { EventBus.getDefault().post(EventObject(KBAMConstant.Events.WS, null))})
        rootView?.rlaOthers?.setOnClickListener(View.OnClickListener { EventBus.getDefault().post(EventObject(KBAMConstant.Events.OTHERS, null)) })

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_screen_share)
        item.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        dashboardActivityContext!!.hideTab()
        dashboardActivityContext!!.hideTOSTab()
        dashboardActivityContext!!.hideOSOTab()
        //getFragmentName();
    }

    override fun getFragmentName(): String {
        return KHomeFragment::class.java.simpleName;
    }
}