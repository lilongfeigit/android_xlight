package com.umarbhutta.xlightcompanion.control.activity.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.App;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.control.adapter.ScenarioSelectListAdapter;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;
import com.umarbhutta.xlightcompanion.okHttp.model.Actioncmd;
import com.umarbhutta.xlightcompanion.okHttp.model.SceneListResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSceneListInfo;
import com.umarbhutta.xlightcompanion.scenario.AddScenarioActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/15.
 * 选择场景
 */

public class SelectScenarioActivity extends AppCompatActivity {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;

    private com.github.clans.fab.FloatingActionButton fab;

    public static String SCENARIO_NAME = "SCENARIO_NAME";
    public static String SCENARIO_INFO = "SCENARIO_INFO";

    public static ArrayList<String> name = null;
    public static ArrayList<String> info = new ArrayList<>(Arrays.asList("A bright, party room preset", "A relaxed atmosphere with yellow tones", "Turn the chandelier rings off"));

    ScenarioSelectListAdapter scenarioListAdapter;
    RecyclerView scenarioRecyclerView;

    public SceneListResult mDeviceInfoResult;

    private Actioncmd mActioncmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scenario);
        name = new ArrayList<String>(Arrays.asList(getString(R.string.beforehand) + " 1", getString(R.string.beforehand) + " 2",getString(R.string.close)));
        //hide nav bar
        getSupportActionBar().hide();

        ((App) getApplicationContext()).setActivity(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.select_scene);
        btnSure.setVisibility(View.GONE);

        mActioncmd = (Actioncmd) getIntent().getSerializableExtra("MACTIONCMD");

        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);

        //setup recycler view
        scenarioRecyclerView = (RecyclerView) findViewById(R.id.scenarioRecyclerView);
        //set LayoutManager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //attach LayoutManager to recycler view
        scenarioRecyclerView.setLayoutManager(layoutManager);
        //divider lines
        scenarioRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabPressed(view);
            }
        });

        getSceneList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String incomingName = data.getStringExtra(SCENARIO_NAME);
                String incomingInfo = data.getStringExtra(SCENARIO_INFO);

                name.add(incomingName);
                info.add(incomingInfo);

                scenarioListAdapter.notifyDataSetChanged();
//                Toast.makeText(this, "The scenario has been successfully added", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this, "No new scenarios were added to the list", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void onFabPressed(View view) {
        Intent intent = new Intent(this, AddScenarioActivity.class);
        startActivityForResult(intent, 1);
    }

    private void getSceneList() {
        RequestSceneListInfo.getInstance().getSceneListInfo(SelectScenarioActivity.this, new RequestSceneListInfo.OnRequestFirstPageInfoCallback() {
            @Override
            public void onRequestFirstPageInfoSuccess(final SceneListResult deviceInfoResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDeviceInfoResult = deviceInfoResult;
                        initList();
                    }
                });
            }

            @Override
            public void onRequestFirstPageInfoFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(SelectScenarioActivity.this, errMsg);
                    }
                });
            }
        });
    }

    private void initList() {
        scenarioListAdapter = new ScenarioSelectListAdapter(SelectScenarioActivity.this, mDeviceInfoResult, mActioncmd);
        scenarioRecyclerView.setAdapter(scenarioListAdapter);
    }
}
