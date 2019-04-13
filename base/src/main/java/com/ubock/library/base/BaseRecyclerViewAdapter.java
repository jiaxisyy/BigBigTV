package com.ubock.library.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ubock.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenGD on 2017/3/16.
 */

public abstract class BaseRecyclerViewAdapter<D, VH extends BaseRecyclerViewAdapter.BaseHolder> extends RecyclerView.Adapter<VH> {
    private Context mContext;
    private List<D> mDataList = new ArrayList<>();
    protected View mSelectItem = null;
    protected View mPreSelectItem = null;
    private int mSelectedIndex = -1;
    private RecyclerView mRecyclerView;

    public interface OnItemSelectedListener<D> {
        void onItemSelected(View view, D data);
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    public interface OnItemChangedListener {
        void onItemChanged(View selectedItem, View preSelectItem);
    }

    /**
     * 拦截点击事件监听器
     */
    public interface OnInteruptClickEventListener {
        /**
         * 是否拦截点击事件，返回true后OnItemSelectedListener、OnItemChangedListener都不会再执行
         * @param adapter
         * @param position
         * @return
         */
        boolean onInterupted(BaseRecyclerViewAdapter adapter, int position);
    }

    protected OnItemSelectedListener mOnItemSelectedListener;
    protected OnItemChangedListener mOnItemChangedListener;
    protected OnItemChildClickListener mOnItemChildClickListener;
    protected OnInteruptClickEventListener mOnInteruptClickEventListener;

    private View.OnClickListener mClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int p = Integer.parseInt(v.getTag(R.id.base_holder_tag_key).toString());
            if (onInteruptClickEvent(p)
                    || (mOnInteruptClickEventListener != null && mOnInteruptClickEventListener.onInterupted(BaseRecyclerViewAdapter.this, p))){
                return;
            }
            mPreSelectItem = mSelectItem;
            setSelectedIndex(p);
            mSelectItem = v;
        }
    };

    /**
     * 是否拦截点击事件，返回true后OnItemSelectedListener、OnItemChangedListener都不会再执行
     * @param position
     * @return
     */
    protected boolean onInteruptClickEvent(int position){
        return false;
    }

    public BaseRecyclerViewAdapter(){}
    public BaseRecyclerViewAdapter(Context context){
        this.mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    @CallSuper
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        holder.itemView.setOnClickListener(mClickListener);
        holder.setAdapter(this);
    }

    @Override
    @CallSuper
    public void onBindViewHolder(VH holder, int position) {
        holder.itemView.setTag(R.id.base_holder_tag_key, position);
        if (position == mSelectedIndex) {
            mSelectItem = holder.itemView;
        }
    }

    public void setSelectedIndex(final int index){
        mSelectedIndex = index;
        if (index < 0 || getDataList() == null || getDataList().size() == 0){
            return;
        }
        if (mRecyclerView != null) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.scrollToPosition(index);
                    View view = mRecyclerView.getLayoutManager().findViewByPosition(index);
                    if (view != null) {
                        onSelectChanged(view, mPreSelectItem);
                        if (mOnItemSelectedListener != null) {
                            mOnItemSelectedListener.onItemSelected(view, getDataList().get(index));
                        }
                    }
                }
            }, 200);
        }
    }

    /**
     * 选择改变时会调用此方法
     * @param selectedItem 当前选择项
     * @param preSelectItem 上次选择项
     * @deprecated
     */
    protected synchronized void onSelectChanged(View selectedItem, View preSelectItem) {
        if (mOnItemChangedListener != null){
            mOnItemChangedListener.onItemChanged(selectedItem, preSelectItem);
        }
    }

    public View getSelectItem(){
        return mSelectItem;
    }

    public View getPreSelectItem(){
        return mPreSelectItem;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 设置数据源，会清空原来的数据
     * @param dataList
     */
    public void setData(List<D> dataList) {
        setData(dataList, -1);
    }

    /**
     * 设置数据源，会清空原来的数据
     * @param dataList
     */
    public void setData(List<D> dataList, int selectIndex) {
        mSelectItem = null;
        mPreSelectItem = null;
        mSelectedIndex = selectIndex;
        if (dataList == null || dataList.size() == 0){
            mSelectedIndex = -1;
        }
        this.mDataList.clear();
        if(null != dataList){
            this.mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
        if(null != dataList && selectIndex >= 0 && selectIndex < dataList.size()) {
            setSelectedIndex(selectIndex);
        }
    }

    /**
     * 获取当前选择项的数据对象
     * @return
     */
    public D getSelectedItemData(){
        return mSelectedIndex >= 0? getDataList().get(mSelectedIndex):null;
    }

    public int getSelectedIndex(){
        return mSelectedIndex;
    }
    /**
     * 追加数据，即向现在数据列表添加数据
     * @param dataList
     */
    public void appendData(List<D> dataList) {
        if (dataList == null) {
            return;
        }
        this.mDataList.addAll(dataList);
        notifyDataSetChanged();

    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    public List<D> getDataList() {
        return mDataList;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }
    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        this.mOnItemChildClickListener = listener;
    }
    public void setOnInteruptClickEventListener(OnInteruptClickEventListener listener) {
        this.mOnInteruptClickEventListener = listener;
    }

    /**
     * @deprecated
     * @param listener
     */
    public void setOnItemChangedListener(OnItemChangedListener listener) {
        this.mOnItemChangedListener = listener;
    }
    public OnItemChildClickListener getOnItemChildClickListener(){
        return mOnItemChildClickListener;
    }

    public static class BaseHolder extends RecyclerView.ViewHolder {
        BaseRecyclerViewAdapter adapter;
        private final SparseArray<View> views = new SparseArray<>();
        public <T extends View> T getView(@IdRes int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }

        protected BaseHolder setAdapter(BaseRecyclerViewAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public BaseHolder setText(@IdRes int viewId, CharSequence value) {
            TextView view = getView(viewId);
            view.setText(value);
            return this;
        }

        public BaseHolder setText(@IdRes int viewId, @StringRes int strId) {
            TextView view = getView(viewId);
            view.setText(strId);
            return this;
        }

        public BaseHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
            ImageView view = getView(viewId);
            view.setImageResource(imageResId);
            return this;
        }

        public BaseHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
            View view = getView(viewId);
            view.setBackgroundColor(color);
            return this;
        }

        public BaseHolder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
            View view = getView(viewId);
            view.setBackgroundResource(backgroundRes);
            return this;
        }

        public BaseHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
            return this;
        }


        public BaseHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
            ImageView view = getView(viewId);
            view.setImageDrawable(drawable);
            return this;
        }

        public BaseHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bitmap);
            return this;
        }

        public BaseHolder setAlpha(@IdRes int viewId, float value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getView(viewId).setAlpha(value);
            } else {
                // Pre-honeycomb hack to set Alpha value
                AlphaAnimation alpha = new AlphaAnimation(value, value);
                alpha.setDuration(0);
                alpha.setFillAfter(true);
                getView(viewId).startAnimation(alpha);
            }
            return this;
        }

        public BaseHolder setVisible(@IdRes int viewId, boolean visible) {
            View view = getView(viewId);
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            return this;
        }

        public BaseHolder linkify(@IdRes int viewId) {
            TextView view = getView(viewId);
            Linkify.addLinks(view, Linkify.ALL);
            return this;
        }

        public BaseHolder setTypeface(@IdRes int viewId, Typeface typeface) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            return this;
        }

        public BaseHolder setTypeface(Typeface typeface, int... viewIds) {
            for (int viewId : viewIds) {
                TextView view = getView(viewId);
                view.setTypeface(typeface);
                view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
            return this;
        }
        public BaseHolder setProgress(@IdRes int viewId, int progress) {
            ProgressBar view = getView(viewId);
            view.setProgress(progress);
            return this;
        }

        public BaseHolder setProgress(@IdRes int viewId, int progress, int max) {
            ProgressBar view = getView(viewId);
            view.setMax(max);
            view.setProgress(progress);
            return this;
        }

        public BaseHolder setMax(@IdRes int viewId, int max) {
            ProgressBar view = getView(viewId);
            view.setMax(max);
            return this;
        }

        public BaseHolder setRating(@IdRes int viewId, float rating) {
            RatingBar view = getView(viewId);
            view.setRating(rating);
            return this;
        }

        public BaseHolder setRating(@IdRes int viewId, float rating, int max) {
            RatingBar view = getView(viewId);
            view.setMax(max);
            view.setRating(rating);
            return this;
        }

        public BaseHolder setTag(@IdRes int viewId, Object tag) {
            View view = getView(viewId);
            view.setTag(tag);
            return this;
        }

        public BaseHolder setTag(@IdRes int viewId, int key, Object tag) {
            View view = getView(viewId);
            view.setTag(key, tag);
            return this;
        }

        public BaseHolder setChecked(@IdRes int viewId, boolean checked) {
            View view = getView(viewId);
            // View unable cast to Checkable
            if (view instanceof Checkable) {
                ((Checkable) view).setChecked(checked);
            }
            return this;
        }

        public BaseHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
            View view = getView(viewId);
            view.setOnClickListener(listener);
            return this;
        }

        public BaseHolder addOnClickListener(@IdRes int viewId) {
            View view = this.getView(viewId);
            if(view != null) {
                if(!view.isClickable()) {
                    view.setClickable(true);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(BaseHolder.this.adapter.getOnItemChildClickListener() != null) {
                            BaseHolder.this.adapter.getOnItemChildClickListener().onItemChildClick(BaseHolder.this.adapter, v, BaseHolder.this.getAdapterPosition());
                        }

                    }
                });
            }

            return this;
        }

        public BaseHolder(View itemView) {
            super(itemView);
        }
    }
}
