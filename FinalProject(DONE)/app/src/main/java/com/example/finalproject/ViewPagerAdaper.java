package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdaper extends FragmentStateAdapter {

    public ViewPagerAdaper(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Tạo và trả về fragment tương ứng với vị trí (position)
        if (position == 0) {
            return new HomeFragment(); // Trang chủ
        } else if (position == 1) {
            return new ProfileFragment(); // Mục yêu thích
        }
        return new HomeFragment(); // Trả về trang chủ mặc định nếu vị trí không khớp
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
