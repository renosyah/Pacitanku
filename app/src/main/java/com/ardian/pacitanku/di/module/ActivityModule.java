package com.ardian.pacitanku.di.module;

import android.app.Activity;

import com.ardian.pacitanku.ui.activity.event.EventActivityContract;
import com.ardian.pacitanku.ui.activity.event.EventActivityPresenter;
import com.ardian.pacitanku.ui.activity.home.HomeActivityContract;
import com.ardian.pacitanku.ui.activity.home.HomeActivityPresenter;
import com.ardian.pacitanku.ui.activity.login.LoginActivityContract;
import com.ardian.pacitanku.ui.activity.login.LoginActivityPresenter;
import com.ardian.pacitanku.ui.activity.register.RegisterActivityContract;
import com.ardian.pacitanku.ui.activity.register.RegisterActivityPresenter;

import dagger.Module;
import dagger.Provides;


// ini adalah class dimana
// setiap melakukan injecksi
// presenter ke activity
// maka akan di provide presenter
// untuk aktivity yg bersangkutan
@Module
public class ActivityModule {

    // dekalrasi variabel activity
    private Activity activity;

    // konstruktor class
    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    // fungsi untuk provide activity
    // dengan nilai balik adalah variabel activity
    // yg telah diinisialisasi
    @Provides
    public Activity provideActivity()  {
        return activity;
    }


    // fungsi untuk provide presenter pada activity login
    @Provides
    public LoginActivityContract.Presenter provideLoginActivityPresenter() {
        return new LoginActivityPresenter();
    }

    // fungsi untuk provide presenter pada activity register
    @Provides
    public RegisterActivityContract.Presenter provideRegisterActivityPresenter() {
        return new RegisterActivityPresenter();
    }

    // fungsi untuk provide presenter pada activity home
    @Provides
    public HomeActivityContract.Presenter provideHomeActivityPresenter() {
        return new HomeActivityPresenter();
    }

    // fungsi untuk provide presenter pada activity event
    @Provides
    public EventActivityContract.Presenter provideEventActivityPresenter() {
        return new EventActivityPresenter();
    }
}
