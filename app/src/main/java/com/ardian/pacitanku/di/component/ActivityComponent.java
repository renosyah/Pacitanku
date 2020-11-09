package com.ardian.pacitanku.di.component;

import com.ardian.pacitanku.di.module.ActivityModule;

import dagger.Component;

// ini adalah interface komponen aktivity
// agar fungsi inject dapat dipanggil
// maka fungsi tersebut sebelumnya harus didelarasi
// di interface ini
@Component(modules = { ActivityModule.class })
public interface ActivityComponent {

    // fungsi yg akan digunakan untuk diinject di activity ExploreActivity
    // void inject(ExploreActivity exploreActivity);

}
