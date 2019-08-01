package com.test.ydool.sanmen.fragments;


public interface FragmentLifecycle {
    void onFragmentPause();
    void onFragmentResume();
    void onBackPressed();
    void onActivityPause();
    void onActivityResume();
    void onActivityDestroy();
}
