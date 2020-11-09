package com.module.hsstestfile.Asyncs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.module.hsstestfile.MyApp;
import com.module.hsstestfile.helper.FilesManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
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
public class CopyUnZipAsyncTask extends AsyncTask<Void, Integer, String> {

    private Context context;
    private FilesManager.LoadFilesListener loadFilesListener;

    public CopyUnZipAsyncTask(Context context, FilesManager.LoadFilesListener loadFilesListener) {
        this.context = context;
        this.loadFilesListener = loadFilesListener;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if ("success".equals(s)) {
            if (loadFilesListener != null) {
                loadFilesListener.copyUnZipSuccess();
            }
        } else {
            if (loadFilesListener != null) {
                loadFilesListener.copyUnZipFail(s);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String loadSuccess = "success";
        try {
            String outFilePath = TO_SD_FILE_PATH + TO_FILE_NAME;
            File filePath = new File(TO_SD_FILE_PATH);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            filePath = new File(outFilePath);
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            InputStream inputStream = null;
            inputStream = context.getAssets().open(ASSETS_NAME);
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(outFilePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[1024];
            int length;
            while (true) {
                if (!((length = inputStream.read(bytes)) > 0)) break;
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
            //关流
            outputStream.close();
            inputStream.close();
            unzip(outFilePath,UNZIP_SD_FILE_PATH);
            if (filePath.exists()) {
                filePath.delete();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            loadSuccess = e.toString();
        } catch (Exception e) {
            e.printStackTrace();
            loadSuccess = e.toString();
        }
        return loadSuccess;
    }


    @SuppressWarnings("unchecked")
    private void unzip(String zipFilePath, String unzipFilePath) throws Exception {
        File zipFile = new File(zipFilePath);
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists()) {
            unzipFileDir.mkdirs();
        }
        //开始解压
        ZipEntry entry = null;
        String entryFilePath = null;
        int count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        //循环对压缩包里的每一个文件进行解压
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            if (entry.getName().contains(".DS_Store")) { continue; }
            entryFilePath = unzipFilePath + File.separator + entry.getName();
            File file = new File(entryFilePath);
            if (entryFilePath.endsWith("/")) {
                if (!file.exists()) {
                    file.mkdir();
                }
                continue;
            }
            bos = new BufferedOutputStream(new FileOutputStream(entryFilePath + "/"));
            bis = new BufferedInputStream(zip.getInputStream(entry));
            while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();
            bos.close();
        }
    }

}
