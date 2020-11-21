package com.ardian.pacitanku.di.component;

import com.ardian.pacitanku.di.module.ActivityModule;
import com.ardian.pacitanku.ui.activity.detailEvent.DetailEventActivity;
import com.ardian.pacitanku.ui.activity.event.EventActivity;
import com.ardian.pacitanku.ui.activity.home.HomeActivity;
import com.ardian.pacitanku.ui.activity.login.LoginActivity;
import com.ardian.pacitanku.ui.activity.register.RegisterActivity;

import dagger.Component;

// ini adalah interface komponen aktivity
// agar fungsi inject dapat dipanggil
// maka fungsi tersebut sebelumnya harus didelarasi
// di interface ini
@Component(modules = { ActivityModule.class })
public interface ActivityComponent {

    // fungsi yg akan digunakan untuk diinject di activity ExploreActivity
    void inject(LoginActivity loginActivity);

    // fungsi yg akan digunakan untuk diinject di activity register
    void inject(RegisterActivity registerActivity);

    // fungsi yg akan digunakan untuk diinject di activity home
    void inject(HomeActivity homeActivity);

    // fungsi yg akan digunakan untuk diinject di activity event
    void inject(EventActivity eventActivity);
}
