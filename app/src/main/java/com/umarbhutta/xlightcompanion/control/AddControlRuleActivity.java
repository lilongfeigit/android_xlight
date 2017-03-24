package com.umarbhutta.xlightcompanion.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.okHttp.model.CreateRuleResult;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rules;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestAddRules;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestDeleteRuleDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umar Bhutta.
 * 新的规则页面
 */
public class AddControlRuleActivity extends AppCompatActivity {
    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private ImageButton ib_add_term, ib_add_result;
    private ListView lv_term, lv_control;

    private TextView tv_no_data1, tv_no_data2;

    private List<String> termList, resultList;
    private Rules rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_control_rule);
        //hide nav bar
        getSupportActionBar().hide();

        rules = new Rules();

        initViews();
    }

    private void initViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定提交按钮
                RequestAddRules.getInstance().createRule(AddControlRuleActivity.this, new Rules(), new RequestAddRules.OnCreateRuleCallback() {

                    @Override
                    public void mOnCreateRuleCallbackFail(int code, String errMsg) {
                        ToastUtil.showToast(AddControlRuleActivity.this, "errMsg=" + errMsg);//TODO
                    }

                    @Override
                    public void mOnCreateRuleCallbackSuccess(CreateRuleResult mCreateRuleResult) {
                        ToastUtil.showToast(AddControlRuleActivity.this, "mCreateRuleResult=" + mCreateRuleResult.msg);//TODO
                    }
                });
            }
        });
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("新建规则");
        ib_add_term = (ImageButton) findViewById(R.id.ib_add_term);
        ib_add_result = (ImageButton) findViewById(R.id.ib_add_result);
        lv_term = (ListView) findViewById(R.id.lv_term);
        lv_control = (ListView) findViewById(R.id.lv_control);

        tv_no_data1 = (TextView) findViewById(R.id.tv_no_data1);
        tv_no_data2 = (TextView) findViewById(R.id.tv_no_data2);

        ib_add_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加启动条件
                onFabPressed(EntryConditionActivity.class);
            }
        });
        ib_add_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加执行结果
                onFabPressed(ControlRuseltActivity.class);
            }
        });

        termList = new ArrayList<String>();
        resultList = new ArrayList<String>();
//        for(int i=0;i<5;i++){
//            termList.add("termList"+i);
//        }
//        for(int i=0;i<5;i++){
//            resultList.add("resultList"+i);
//        }
        lv_term.setAdapter(new TermAdapter(getApplicationContext(), termList));
        lv_control.setAdapter(new ResultAdapter(getApplicationContext(), resultList));
    }

    private void onFabPressed(Class activity) {
        Intent intent = new Intent(this, activity);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DEVICE_CONTROL", rules);
        intent.putExtra("BUNDLE", bundle);
        startActivityForResult(intent, 2018);
    }

    /**
     * 添加规则
     */
    private class TermAdapter extends BaseAdapter {

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<String> mTermsList;

        private TermAdapter(Context context, List<String> termsList) {
            this.mContext = context;
            this.mTermsList = termsList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mTermsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTermsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_term, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.tvStr = (TextView) convertView.findViewById(R.id.tv_str);
                holder.imageView = (ImageButton) convertView.findViewById(R.id.ib_minus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvStr.setText(mTermsList.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                    ToastUtil.showToast(AddControlRuleActivity.this, "删除" + position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tvStr;
            private ImageButton imageView;
        }
    }

    /**
     * 执行结果
     */
    private class ResultAdapter extends BaseAdapter {

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<String> mResultsList;

        private ResultAdapter(Context context, List<String> resultsList) {
            this.mContext = context;
            this.mResultsList = resultsList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mResultsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mResultsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_result, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.tvStr = (TextView) convertView.findViewById(R.id.tv_result);
                holder.imageView = (ImageButton) convertView.findViewById(R.id.ib_minus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvStr.setText(mResultsList.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                    ToastUtil.showToast(AddControlRuleActivity.this, "删除" + position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tvStr;
            private ImageButton imageView;
        }
    }
}
