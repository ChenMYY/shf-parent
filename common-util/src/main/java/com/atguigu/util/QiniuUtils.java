package com.atguigu.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 包名:com.atguigu.util
 *
 * @author Leevi
 * 日期2022-03-27  19:52
 */
public class QiniuUtils {
    private static String accessKey = "Qo3PDldD7TS1sHd4kA6ZlcscTA4uFNelrDljU0-S";
    private static String secretKey = "gcUnNVkSNKSIdNHJ11FMTztK1RLGC4QLlZ3yhckm";
    private static String bucket = "shf-221026";

    /**
     * 上传图片到七牛云
     * @param localFilePath  要上传的文件在本地的路径
     * @param filePath  文件要上传到七牛云中的路径
     */
    public static void upload2Qiniu(String localFilePath,String filePath){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, filePath, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 上传文件到七牛云
     * @param bytes  要上传的文件转成的字节数组
     * @param filePath  文件存储到七牛云中的路径
     */
    public static void upload2Qiniu(byte[] bytes, String filePath){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 从七牛云中删除文件
     * @param filePath  要删除的文件在七牛云中的路径
     */
    public static void deleteFileFromQiniu(String filePath){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        String key = filePath;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 获取七牛云中的图片的访问路径
     * @param filePath  图片在七牛云中的路径
     * @return
     */
    public static String getUrl(String filePath) {
        return "http://rpu53pb9u.hn-bkt.clouddn.com/" + filePath;
    }
}
