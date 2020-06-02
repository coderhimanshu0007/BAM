package com.teamcomputers.bam.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Fragments.home.HomeViewModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.RatingBars.FacesRatingBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HelpFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    LoginModel userProfile;
    private DashboardActivity dashboardActivityContext;
    private HomeViewModel homeViewModel;
    String toolbarTitle = "";
    /*@BindView(R.id.ratingbar_faces)
    FacesRatingBar ratingbar_faces;*/
    @BindView(R.id.wviHelp)
    WebView wviHelp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_help, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        dashboardActivityContext = (DashboardActivity) context;
        toolbarTitle = getString(R.string.Heading_Feedback);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        /*ratingbar_faces.setValueSetListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                showToast("" + integer);
                return null;
            }
        });

        ratingbar_faces.setValueChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                //showToast("Change" + integer);
                return null;
            }
        });*/

        WebSettings settings = wviHelp.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        wviHelp.loadUrl("https://www.kockpit.in/kockpit-business-xray/");
        // Force links and redirects to open in the WebView instead of in a browser
        wviHelp.setWebViewClient(new WebViewClient());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_screen_share);
        item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getFragmentName() {
        return HelpFragment.class.getSimpleName();
    }

    /*@OnClick(R.id.btn_submit)
    public void Submit() {
        showToast(ToastTexts.WORK_PROGRESS);
        //EventBus.getDefault().post(new EventObject(Events.OTHERS, null));
    }*/
}