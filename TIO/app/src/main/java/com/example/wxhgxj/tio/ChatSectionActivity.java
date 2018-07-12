package com.example.wxhgxj.tio;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatSectionActivity extends AppCompatActivity {

    private ChatPagerAdapter mChatPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_session);

        mChatPagerAdapter = new ChatPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.chatSessionPager);
        mViewPager.setAdapter(mChatPagerAdapter);
        mTabLayout = (TabLayout)findViewById(R.id.chatTabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

}
