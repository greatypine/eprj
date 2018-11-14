package com.guoanshequ.eprj.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @ProjectName: sp
 * @Package: com.guoanshequ.utils
 * @Description: ftp 工具类
 * @Author: gbl
 * @CreateDate: 2018/11/5 14:18
 */

public class FTPClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPClientUtil.class);

    private   FTPClient ftpClient;
    //ftp IP
    private   String ftpServer;
    //ftp 登录名
    private   String username;
    //ftp 登录密码
    private   String password;
    //ftp服务器存储地址
    private String remoteFileDir;


    public FTPClientUtil(String ftpServer,String username,String password,String remoteFileDir){
        this.ftpServer = ftpServer;
        this.username = username;
        this.password = password;
        this.remoteFileDir = remoteFileDir;
    }



    /**
     * @Description 非代理模式连接ftp
     * @author gbl
     * @date 2018/11/5 16:22
     **/

    public boolean openConnect() {

        boolean connectStatus=true;
        try {
            ftpClient = new FTPClient();
            // 中文转码
            ftpClient.setControlEncoding("UTF-8");
            FTPClientConfig config = new FTPClientConfig();
            ftpClient.configure(config);
            ftpClient.connect(ftpServer);
            ftpClient.login(username, password);
            System.out.printf("Connected to %s .", ftpServer);
            System.out.printf("%s \n", ftpClient.getReplyString());
            // After connection attempt, you should check the reply code to verify
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                connectStatus = false;
                System.err.println("FTP server refused connection.");
                logger.info("FTP server refused connection.");
            }


        } catch (IOException e) {
            connectStatus = false;
            e.printStackTrace();
            logger.info("登录FTP失败>>>>>"+e.getMessage());
            return false;
        }


        return connectStatus;
    }


    /**
     * @Description 上传文件到ftp
     * @param localFile 本地文件
     * @param remoteFile  远程ftp 服务器文件名
     * @author gbl
     * @date 2018/11/5 16:36
     **/

    public boolean uploadFile(File localFile,String remoteFile){

        openConnect();

        FileInputStream fileInputStream =null;
        boolean uploadResult=true;
        try {
            fileInputStream =new  FileInputStream(localFile);
            ftpClient.changeWorkingDirectory(remoteFileDir);
            uploadResult = ftpClient.storeFile(remoteFile, fileInputStream);
            System.out.printf("upload result: %s.\n", uploadResult);
            fileInputStream.close();
            logger.info("upload result: %s.\n", uploadResult);
        } catch (IOException e) {
            uploadResult=false;
            System.out.println("upload file fail! ->caused by:"+e.getMessage());
            e.printStackTrace();
            logger.info("上传文件失败>>>>>"+e.getMessage());
        } finally {

            if(fileInputStream!=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("关闭文件流失败>>>>>"+e.getMessage());
                }
            }
        }

        return uploadResult;
    }

    /**
     * @Description 关闭ftp
     * @author gbl
     * @date 2018/11/5 16:44
     **/

    public boolean closeConnect(){
        boolean result = true;
        try {
            result = ftpClient.logout();
        } catch (IOException e) {
            result=false;
            System.out.println("close ftp fail! -> caused by:"+e.getMessage());
            e.printStackTrace();
            logger.info("退出FTP失败>>>>>"+e.getMessage());
            return result;
        } finally {
            if(ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch(IOException ioe) {
                    logger.info("关闭FTP连接失败>>>>>"+ioe.getMessage());
                }
            }
        }

        return result;
    }



}
