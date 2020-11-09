package com.module.hsstestfile.Asyncs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.module.hsstestfile.MainActivity;
import com.module.hsstestfile.helper.FilesManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.zip.Adler32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import static android.text.TextUtils.isEmpty;
import static com.module.hsstestfile.helper.FilesManager.ASSETS_NAME;
import static com.module.hsstestfile.helper.FilesManager.TO_FILE_NAME;
import static com.module.hsstestfile.helper.FilesManager.TO_SD_FILE_PATH;
import static com.module.hsstestfile.helper.FilesManager.UNZIP_SD_FILE_PATH;

/**
 * @author Hss
 * @time 2020/11/4 23:21
 * @describe
 **/
public class ReadFilesAsyncTask extends AsyncTask<Void, Integer, String> {

    private Context context;
    private FilesManager.LoadFilesListener loadFilesListener;
    public static final String LOG_TAG = "FileScanner";
    public ArrayList<String> mFileList = new ArrayList<>();

    private boolean mIsRunning = true;

    public ReadFilesAsyncTask(Context context, FilesManager.LoadFilesListener loadFilesListener) {
        this.context = context;
        this.loadFilesListener = loadFilesListener;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if ("success".equals(s)) {
            if (loadFilesListener != null) {
                loadFilesListener.getFilesSuccess(mFileList);
            }
        } else {
            if (loadFilesListener != null) {
                loadFilesListener.getFilesFail(s);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (context instanceof MainActivity){
            ((MainActivity)context).showLoadFilePb();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(Void... voids) {
        String loadSuccess = "success";
        try {
            scanFiles();
        }catch (Exception e){
            e.printStackTrace();
            loadSuccess = e.toString();
        }
        return loadSuccess;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void scanFiles(){
        mFileList.clear();
        File file = new File(UNZIP_SD_FILE_PATH);
        Stack<File> folderStack = new Stack<>();
        folderStack.push(file);
        while (!folderStack.empty()){
            if(!mIsRunning){
                break;
            }
            file = folderStack.pop();
            File[] files = file.listFiles();

            //　内部排序
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                }
            });

            for(int i = 0; i < files.length;  i++){
                String fullfilepath = files[i].getAbsolutePath();

                if(files[i].isDirectory() && !"./".equals(files[i].getName())){
                    folderStack.push(files[i]);
                    mFileList.add(fullfilepath);
                } else {
                    mFileList.add(fullfilepath);
                }
            }
        }
        Arrays.sort(mFileList.toArray());
        //　外部排序
        mFileList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

}
