package com.umarbhutta.xlightcompanion.scenario;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.model.SceneListResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestDeleteScene;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSceneListInfo;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Umar Bhutta.
 */
public class ScenarioFragment extends Fragment {

    private com.github.clans.fab.FloatingActionButton fab;

    public static String SCENARIO_NAME = "SCENARIO_NAME";
    public static String SCENARIO_INFO = "SCENARIO_INFO";

    public static ArrayList<String> name = new ArrayList<>(Arrays.asList("Preset 1", "Preset 2", "Turn off"));
    public static ArrayList<String> info = new ArrayList<>(Arrays.asList("A bright, party room preset", "A relaxed atmosphere with yellow tones", "Turn the chandelier rings off"));

    ScenarioListAdapter scenarioListAdapter;
    RecyclerView scenarioRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenario, container, false);

        fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab);

        //setup recycler view
        scenarioRecyclerView = (RecyclerView) view.findViewById(R.id.scenarioRecyclerView);
        //create list adapter
        //TODO
        //set LayoutManager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //attach LayoutManager to recycler view
        scenarioRecyclerView.setLayoutManager(layoutManager);
        //divider lines
        scenarioRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabPressed(view);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSceneList();
    }

    /**
     * 删除场景
     */
    private void showDeleteSceneDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除场景提示");
        builder.setMessage("确定删除此场景吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteScene(position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void deleteScene(final int position) {
        Rows mSceneInfo = mDeviceInfoResult.rows.get(position);
        RequestDeleteScene.getInstance().deleteScene(getActivity(), mSceneInfo.id, new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getActivity(), "删除成功");
                        mDeviceInfoResult.rows.remove(position);
                        scenarioListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getActivity(), "" + errMsg);
                    }
                });
            }
        });
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
                Toast.makeText(getActivity(), "The scenario has been successfully added", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "No new scenarios were added to the list", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onFabPressed(View view) {
        Intent intent = new Intent(getContext(), AddScenarioActivity.class);
        startActivityForResult(intent, 1);
    }

    public SceneListResult mDeviceInfoResult;

    private void getSceneList() {
        RequestSceneListInfo.getInstance().getSceneListInfo(getActivity(), new RequestSceneListInfo.OnRequestFirstPageInfoCallback() {
            @Override
            public void onRequestFirstPageInfoSuccess(final SceneListResult deviceInfoResult) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDeviceInfoResult = deviceInfoResult;
                        initList();
                    }
                });
            }

            @Override
            public void onRequestFirstPageInfoFail(int code, final String errMsg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getActivity(), ""+errMsg);
                    }
                });
            }
        });
    }

    private void initList() {
        scenarioListAdapter = new ScenarioListAdapter(getContext(), mDeviceInfoResult);
        scenarioListAdapter.setOnLongClickCallBack(new ScenarioListAdapter.OnLongClickCallBack() {
            @Override
            public void onLongClickCallBack(int position) {
                showDeleteSceneDialog(position);
            }
        });
        scenarioRecyclerView.setAdapter(scenarioListAdapter);
    }

}
