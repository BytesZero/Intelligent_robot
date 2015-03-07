package com.zhengsonglan.cold.adapter.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder简洁的写法
 * Created by zsl on 2014/12/12.
 */
public class ViewHolderUtil {


    public static <T extends View> T get(View view, int id) {

        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {

            viewHolder = new SparseArray<View>();

            view.setTag(viewHolder);

        }

        View childView = viewHolder.get(id);

        if (childView == null) {

            childView = view.findViewById(id);

            viewHolder.put(id, childView);
        }

        return (T) childView;

    }
}
