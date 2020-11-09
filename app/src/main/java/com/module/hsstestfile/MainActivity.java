package com.module.hsstestfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.module.hsstestfile.adapter.FilesAdapter;
import com.module.hsstestfile.helper.FilesManager;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,FilesManager.LoadFilesListener {

    private ProgressBar  filesPb;
    private RecyclerView filesRl;
    private FilesManager filesManager;
    private int clickType = -1;
    private FilesAdapter filesAdapter;

    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        requestPermision();
        filesManager = new FilesManager(this);
        filesManager.setLoadingListener(this);
        initData();
    }

    private void initView() {
        filesRl = findViewById(R.id.id_rl);
        filesPb = findViewById(R.id.load_pb);
    }

    private void initListener() {
        findViewById(R.id.id_copy_unzip).setOnClickListener(this);
        findViewById(R.id.id_getfiles).setOnClickListener(this);
    }

    private void requestPermision(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }else {
                handleClick(clickType);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            int successCode = 0;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i]!=0){
                    successCode = -1;
                }
            }
            if (successCode == 0){
                handleClick(clickType);
            }
        }
    }

    private void initData(){
        filesAdapter = new FilesAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        filesRl.setLayoutManager(linearLayoutManager);
        filesRl.setAdapter(filesAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_copy_unzip:
                clickType = 0;
                requestPermision();
                break;
            case R.id.id_getfiles:
                if (!FilesManager.hasUnZip){
                    Toast.makeText(MainActivity.this,"请先解压文件",Toast.LENGTH_SHORT).show();
                    return;
                }
                handleClick(1);
                break;
            default:
                break;
        }
    }

    private void handleClick(int clickType){
        if (clickType == 0){ //复制+解压
            filesManager.copyUnZip();
        }else if (clickType == 1){
            filesManager.readFiles();//读取文件
        }
    }

    /*显示loading*/
    public void showLoadFilePb(){
        if (filesPb.getVisibility()!=View.VISIBLE){
            filesPb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void copyUnZipSuccess() {
        FilesManager.hasUnZip = true;
        Toast.makeText(this,"解压完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void copyUnZipFail(String copyError) {
        Log.e("ceshi"," error:  " + copyError);
        Toast.makeText(this,"解压失败:" + copyError,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getFilesSuccess(List<String> filesData) {
        filesPb.setVisibility(View.INVISIBLE);
        filesAdapter.setData(filesData);
    }

    @Override
    public void getFilesFail(String readError) {
        Log.e("ceshi"," error:  " + readError);
        Toast.makeText(this,"读取失败:" + readError,Toast.LENGTH_SHORT).show();
    }
}
