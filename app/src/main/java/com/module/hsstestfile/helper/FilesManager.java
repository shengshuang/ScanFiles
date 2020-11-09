package com.module.hsstestfile.helper;

import android.content.Context;
import android.os.Environment;
import com.module.hsstestfile.Asyncs.CopyUnZipAsyncTask;
import com.module.hsstestfile.Asyncs.ReadFilesAsyncTask;

import java.util.List;

/**
 * @author Hss
 * @time 2020/11/4 21:35
 * @describe
 **/
public class FilesManager {

    public static final String ASSETS_NAME = "test_file.zip";
    public static final String TO_SD_FILE_PATH = Environment.getExternalStorageDirectory() + "/hss_test/files/";
    public static final String UNZIP_SD_FILE_PATH = Environment.getExternalStorageDirectory() + "/hss_test/unzip_files";
    public static final String TO_FILE_NAME = "test_file.zip";

    public static boolean hasUnZip = false;

    private Context context;
    public FilesManager(Context context){
        this.context = context;
        hasUnZip = false;
    }

    /*复制+解压*/
    public void copyUnZip() {
        new CopyUnZipAsyncTask(context,loadFilesListener).execute();
    }

    /*读取文件*/
    public void readFiles() {
        new ReadFilesAsyncTask(context,loadFilesListener).execute();
    }

    private LoadFilesListener loadFilesListener;
    public void setLoadingListener(LoadFilesListener loadingListener) {
        this.loadFilesListener = loadingListener;
    }

    public interface LoadFilesListener {

        void copyUnZipSuccess();
        void copyUnZipFail(String copyError);

        void getFilesSuccess(List<String> filesData);
        void getFilesFail(String readError);

    }


}
