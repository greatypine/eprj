package com.guoanshequ.eprj.controller;

import com.guoanshequ.eprj.util.FTPClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author daishuhua
 * @date 2018/11/7 10:54
 */
@RestController
public class FtpTransferController {

    @Value("${eprj.ndrc.ftpServer}")
    private String ftpServer;

    @Value("${eprj.ndrc.userName}")
    private String ftpUserName;

    @Value("${eprj.ndrc.passWord}")
    private String ftpPassWord;

    @Value("${eprj.ndrc.fileDir}")
    private String remoteFileDir;



    @RequestMapping(value = "/upload", consumes = "multipart/form-data", method = RequestMethod.POST)
    public void transfer2Ftp(@RequestParam("file")MultipartFile file) {

        System.out.printf("%s\n", file.getOriginalFilename());
        // 判断文件是否存在
        if (!file.isEmpty()) {

            String path = "/jgjc_upload/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            path += file.getOriginalFilename();
            File localFile = new File(path);
            try {
                file.transferTo(localFile);
                FTPClientUtil ftpClientUtil = new FTPClientUtil(ftpServer,ftpUserName,ftpPassWord,remoteFileDir);
                ftpClientUtil.uploadFile(localFile,localFile.getName());
                ftpClientUtil.closeConnect();
            } catch (IllegalStateException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/upload-multi", consumes = "multipart/form-data", method = RequestMethod.POST)
    public void transfer2Ftp(HttpServletRequest request) {
        String pathname = "/jgjc_upload/";
        File file = new File(pathname);
        if (!file.exists()) {
            file.mkdirs();
        }
        MultipartHttpServletRequest muti = (MultipartHttpServletRequest) request;
        System.out.println(muti.getMultiFileMap().size());

        FTPClientUtil ftpClientUtil = new FTPClientUtil(ftpServer,ftpUserName,ftpPassWord,remoteFileDir);

        MultiValueMap<String, MultipartFile> map = muti.getMultiFileMap();
        for (Map.Entry<String, List<MultipartFile>> entry : map.entrySet()) {

            List<MultipartFile> list = entry.getValue();
            for (MultipartFile multipartFile : list) {
                try {

                    File uploadFile = new File(pathname + multipartFile.getOriginalFilename());
                    multipartFile.transferTo(uploadFile);
                    ftpClientUtil.uploadFile(uploadFile,uploadFile.getName());
                    ftpClientUtil.closeConnect();
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
