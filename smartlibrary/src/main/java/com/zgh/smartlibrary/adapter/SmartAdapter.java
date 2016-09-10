package com.zgh.smartlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 朱国辉
 * Date: 2015/12/5
 * Time: 22:26
 *
 */
public abstract class SmartAdapter<E> extends BaseAdapter {

    protected Context mContext;
    private List<E> data;

    public Map<Integer, SmartViewHolder> holderMap = new HashMap<>();

    public SmartAdapter(Context ctx, List<E> data,
                        SmartViewHolder... holders) {
        this.mContext = ctx;
        this.data = data;
        if (holders != null && holders.length > 0) {
            for (int i = 0; i < holders.length; i++) {
                holders[i].setType(i);
                holderMap.put(holders[i].getViewType(), holders[i]);
            }
        } else {
            throw new IllegalArgumentException("SmartViewHolder 不能为空");
        }

    }

    public SmartAdapter(Context ctx, List<E> data,
                        List<SmartViewHolder> holders) {
        this.mContext = ctx;
        this.data = data;
        int i = 0;
        if (holders != null && holders.size() > 0) {
            for (SmartViewHolder holder : holders) {
                holder.setType(i++);
                holderMap.put(holder.getViewType(), holder);
            }
        } else {
            throw new IllegalArgumentException("SmartViewHolder 不能为空");
        }

    }

    @Override
    public E getItem(int position) {
        if (!isEmpty(data)) {
            return data.get(position);
        }
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return holderMap.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (!isEmpty(data)) {
            return data.size();
        }
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SmartViewHolder holder;
        if (convertView == null) {
            int type = getItemViewType(position);
            convertView = LayoutInflater.from(mContext).inflate(holderMap.get(type).getLayoutId(), parent, false);
            holder = buildHolder(convertView, type);
            convertView.setTag(holder);
        } else {
            holder = (SmartViewHolder) convertView.getTag();
        }

        // 避免Item在滚动中出现黑色背景
        convertView.setDrawingCacheEnabled(false);
        E item = getItem(position);
        holder.setContentView(convertView);
        holder.updateView(mContext, item);
        return convertView;
    }

    private SmartViewHolder buildHolder(View convertView, int type) {
        SmartViewHolder holder;
        try {
            holder = holderMap.get(type).getClass().newInstance();
            List<Field> fields = getViewFields(holder.getClass());
            for (Field f : fields) {
                String name = f.getName();
                f.setAccessible(true);
                // ViewHolder的属性，不论类型都初始化赋值
                f.set(holder, convertView.findViewById(getId(name)));
            }
        } catch (Exception e) {
            throw new RuntimeException("holder初始化出错    " + e);
        }
        return holder;
    }

    private List<Field> getViewFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != SmartViewHolder.class) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (isViewField(f)) {
                    fields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private boolean isViewField(Field f) {
        Class<?> fType = f.getType();
        boolean isView = false;
        Class sclass = fType;
        while (sclass != null && sclass != View.class) {
            sclass = sclass.getSuperclass();
        }
        if (sclass == View.class) {
            isView = true;
        }
        return isView;
    }


    public void addItems(List<E> extras) {
        if (isEmpty(extras)) {
            return;
        }
        data.addAll(getCount(), extras);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Collection<SmartViewHolder> holders = holderMap.values();
        for (SmartViewHolder holder : holders) {
            if (holder.isMyType(data.get(position))) {
                return holder.getViewType();
            }
        }
        throw new RuntimeException("没有对应的 SmartViewHolder position=" + position + " item=" + data.get(position));
    }


    /**
     * Some General Functions
     */
    private boolean isEmpty(List<?> list) {
        return (list == null || list.size() == 0);
    }


    public int getId(String name) {
        try {
            return mContext.getResources().getIdentifier(name, "id", mContext.getPackageName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static abstract class SmartViewHolder<E> {
        int type;
        private View contentView;

        /**
         * 获取该VIewHolder对应的Type
         *
         * @return
         */
        public final int getViewType() {
            return type;
        }

        /**
         * 判断是否是自己处理的数据类型
         *
         * @param item
         * @return
         */
        public abstract boolean isMyType(E item);

        public void setType(int type) {
            this.type = type;
        }

        /**
         * 获取对应的布局id
         *
         * @return
         */
        public abstract int getLayoutId();

        public abstract void updateView(Context context, E item);

        public void setContentView(View contentView) {
            this.contentView = contentView;
        }

        public View getContentView() {
            return this.contentView;
        }

    }
}
