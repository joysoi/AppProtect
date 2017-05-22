package com.nikola.appprotect.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikola.appprotect.R;
import com.nikola.appprotect.database.AppsContentProvider;
import com.nikola.appprotect.database.AppsDb;
import com.nikola.appprotect.util.GlideUtil;
import com.nikola.appprotect.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AppAdapter extends CursorAdapter {

    public interface OnLockClick {
        void onClick();
    }

    private Context context;
    private OnLockClick onLockClick;

    public AppAdapter(Context context, Cursor c, int flags, OnLockClick onLockClick) {
        super(context, c, flags);
        this.context = context;
        this.onLockClick = onLockClick;
    }

    class ViewHolder implements View.OnClickListener {
        @BindView(R.id.icon)
        ImageView iconView;
        @BindView(R.id.txt_label)
        TextView label;
        @BindView(R.id.img_fake_lock_off)
        ImageView lockImageView;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            lockImageView.setOnClickListener(this);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                if (!PermissionUtil.isUsageStatsPermissionGranted(context))
                    PermissionUtil.showEnableUsageAccessDialog((Activity) context);
            }
            int array[] = (int[]) v.getTag();
            Timber.d(String.valueOf(array[0]));
            ContentValues values = new ContentValues();
            values.put(AppsDb.COLUMN_LOCKED_STATUS, array[1] == 1 ? 0 : 1);
            context.getContentResolver().update(AppsContentProvider.BASE_CONTENT_URI, values, AppsDb.COLUMN_ID + "=?", new String[]{String.valueOf(array[0])});
            onLockClick.onClick();
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_icon_text_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String appName = cursor.getString(cursor.getColumnIndexOrThrow(AppsDb.COLUMN_LABEL));
        viewHolder.label.setText(appName);
        String pckgName = cursor.getString(cursor.getColumnIndexOrThrow(AppsDb.COLUMN_PACKAGE_NAME));
        GlideUtil.setAppIcon(context, viewHolder.iconView,
                pckgName);
        int lockStatus = cursor.getInt(cursor.getColumnIndexOrThrow(AppsDb.COLUMN_LOCKED_STATUS));
        viewHolder.lockImageView.setImageResource(
                lockStatus == 1 ? R.drawable.ic_thumb_up_30dp : R.drawable.ic_thumb_down_30dp);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(AppsDb.COLUMN_ID));
        viewHolder.lockImageView.setTag(new int[]{id, lockStatus});
    }
}