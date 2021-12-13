package com.acap.adapter.slide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Tip:
 *
 *
 * @author A·Cap
 * @date 2021/12/10 18:19
 * </pre>
 */
public class SlideMenuProvider {
    // Menus
    private final List<SlideMenu> mMenus = new ArrayList<>();
    // MenuId -> Menu
    private final Map<Integer, SlideMenu> mIdMenus = new HashMap<>();
//    private final Map<Integer, SlideMenu> mLayoutMenus = new HashMap<>();

    public boolean isEmpty() {
        return mMenus.isEmpty();
    }

    public void add(SlideMenu menu) {
        int id = menu.getId();
        if (getMenuById(id) != null) {
            throw new IllegalStateException("Cannot insert SlideMenu with the same ID:" + menu.getId());
        }
        mIdMenus.put(id, menu);
        mMenus.add(menu);
    }

    public SlideMenu getMenuById(int layout) {
        return mIdMenus.get(layout);
    }

    public List<SlideMenu> getMenus() {
        return mMenus;
    }

    public int getCount() {
        return mMenus.size();
    }


}
