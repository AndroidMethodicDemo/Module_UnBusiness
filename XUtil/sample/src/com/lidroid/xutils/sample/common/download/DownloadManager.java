package com.lidroid.xutils.sample.common.download;

import android.content.Context;
import android.database.Cursor;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载管理类
 * 1. 
 * 2. 
 * 3. 
 */
public class DownloadManager {

	// 核心数据
    private List<DownloadInfo> downloadInfoList;

    // 默认最大下载线程数
    private int maxDownloadThread = 3;

    private Context mContext;
    private DbUtils db;

    /**
     * 构造方法
     * 1. 
     * @param appContext
     */
    /*package*/ DownloadManager(Context appContext) {
    	// 注册行转化器，HttpHandlerStateConverter是个内部类
        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class, new HttpHandlerStateConverter());
        mContext = appContext;
        db = DbUtils.create(mContext);
        try {
            downloadInfoList = db.findAll(Selector.from(DownloadInfo.class));
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
        if (downloadInfoList == null) {
            downloadInfoList = new ArrayList<DownloadInfo>();
        }
    }

    /**
     * 便捷方法：获取数量
     */
    public int getDownloadInfoListCount() {
        return downloadInfoList.size();
    }

    /**
     * 便捷方法：获取信息
     */
    public DownloadInfo getDownloadInfo(int index) {
        return downloadInfoList.get(index);
    }

    /**
     * 核心方法
     * @param url
     * @param fileName
     * @param target
     * @param autoResume
     * @param autoRename
     * @param callback
     * @throws DbException
     */
    public void addNewDownload(String url, String fileName, String target,
                               boolean autoResume, boolean autoRename,
                               final RequestCallBack<File> callback) throws DbException {
    	// 新建DownloadInfo
        final DownloadInfo downloadInfo = new DownloadInfo();
        // 设置DownloadInfo
        downloadInfo.setDownloadUrl(url);
        downloadInfo.setAutoRename(autoRename);
        downloadInfo.setAutoResume(autoResume);
        downloadInfo.setFileName(fileName);
        downloadInfo.setFileSavePath(target);
        //新建HttpUtils
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        //调用HttpUtils的方法download进行下载，返回HttpHandler
		HttpHandler<File> handler = http.download(url, target, autoResume,
				autoRename, new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        // 将downloadInfo添加到DownloadInfoList
        downloadInfoList.add(downloadInfo);
        // 将DownloadInfo添加到数据库
        db.saveBindingId(downloadInfo);
    }

    public void resumeDownload(int index, final RequestCallBack<File> callback) throws DbException {
        final DownloadInfo downloadInfo = downloadInfoList.get(index);
        resumeDownload(downloadInfo, callback);
    }

    /**
     * 重新下载
     * @param downloadInfo
     * @param callback
     * @throws DbException
     */
    public void resumeDownload(DownloadInfo downloadInfo, final RequestCallBack<File> callback) throws DbException {
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(
                downloadInfo.getDownloadUrl(),
                downloadInfo.getFileSavePath(),
                downloadInfo.isAutoResume(),
                downloadInfo.isAutoRename(),
                new ManagerCallBack(downloadInfo, callback));
        //私信设置Handler和State
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        //更新数据库
        db.saveOrUpdate(downloadInfo);
    }

    /**
     * 便捷方法：传入索引，移除下载项
     * @param index
     * @throws DbException
     */
    public void removeDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        removeDownload(downloadInfo);
    }

    /**
     * 移除下载项
     * 1. 如果还在下载，则调用HttpHandler的cancel方法取消下载
     * 2. 从内存List中移除
     * 3. 从数据库中移除
     * @param downloadInfo
     * @throws DbException
     */
    public void removeDownload(DownloadInfo downloadInfo) throws DbException {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
        }
        downloadInfoList.remove(downloadInfo);
        db.delete(downloadInfo);
    }

    /**
     * 便捷方法，传入索引，停止下载
     * @param index
     * @throws DbException
     */
    public void stopDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }

    /**
     * 停止下载，与移除下载相比都是先cancel，不同的是在cancel之后
     * 1. 如果Handler为null，置为canceled状态
     * 2. 更新数据库
     * @param downloadInfo
     * @throws DbException
     */
    public void stopDownload(DownloadInfo downloadInfo) throws DbException {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
        } else {
            downloadInfo.setState(HttpHandler.State.CANCELLED);
        }
        db.saveOrUpdate(downloadInfo);
    }

    /**
     * 便捷方法：停止所有下载项
     */
    public void stopAllDownload() throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null && !handler.isCancelled()) {
                handler.cancel();
            } else {
                downloadInfo.setState(HttpHandler.State.CANCELLED);
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    /**
     * ？？？？不是很重要的方法
     * @throws DbException
     */
    public void backupDownloadInfoList() throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    /**
     * 配置方法：获取最大下载线程
     * @return
     */
    public int getMaxDownloadThread() {
        return maxDownloadThread;
    }

    /**
     * 配置方法：设置最大下载线程
     * @param maxDownloadThread
     */
    public void setMaxDownloadThread(int maxDownloadThread) {
        this.maxDownloadThread = maxDownloadThread;
    }

    /**
     * 
     *
     */
    public class ManagerCallBack extends RequestCallBack<File> {
        private DownloadInfo downloadInfo;
        private RequestCallBack<File> baseCallBack;

        public RequestCallBack<File> getBaseCallBack() {
            return baseCallBack;
        }

        public void setBaseCallBack(RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
        }

        /**
         * 构造方法
         * @param downloadInfo
         * @param baseCallBack
         */
        private ManagerCallBack(DownloadInfo downloadInfo, RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
            this.downloadInfo = downloadInfo;
        }

        @Override
        public Object getUserTag() {
            if (baseCallBack == null) return null;
            return baseCallBack.getUserTag();
        }

        @Override
        public void setUserTag(Object userTag) {
            if (baseCallBack == null) return;
            baseCallBack.setUserTag(userTag);
        }

        /**
         * 开始下载
         * 1. 设置DownloadInfo的state
         * 2. 保存DownloadInfo到数据库
         * 3. 调用用户的callBack
         */
        @Override
        public void onStart() {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onStart();
            }
        }

        /**
         * 类似onStart
         */
        @Override
        public void onCancelled() {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onCancelled();
            }
        }

        /**
         * 设置相关信息到DownloadInfo
         * 调用用户的callBack
         */
        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            downloadInfo.setFileLength(total);
            downloadInfo.setProgress(current);
            try {
            	//每时每刻都在更新数据库，真的好么？？
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onLoading(total, current, isUploading);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onSuccess(responseInfo);
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onFailure(error, msg);
            }
        }
    }

    /**
     * 行转化器ColumnConverter
     */
    private class HttpHandlerStateConverter implements ColumnConverter<HttpHandler.State> {

        @Override
        public HttpHandler.State getFieldValue(Cursor cursor, int index) {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFieldValue(String fieldStringValue) {
            if (fieldStringValue == null) return null;
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
            return fieldValue.value();
        }

        @Override
        public ColumnDbType getColumnDbType() {
            return ColumnDbType.INTEGER;
        }
    }
}
