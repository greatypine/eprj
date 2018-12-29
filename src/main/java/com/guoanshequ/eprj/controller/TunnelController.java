package com.guoanshequ.eprj.controller;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenchuang
 * @Date: 2018/12/28 12:35
 */
@RestController
public class TunnelController {

    @Value("${eprj.sqlserver.url}")
    private String sqlserverUrl;

    @Value("${eprj.sqlserver.user}")
    private String sqlserverUser;

    @Value("${eprj.sqlserver.password}")
    private String sqlserverPwd;

    @RequestMapping("/queryData.action")
    @ResponseBody
    public List tunnelExcute(@RequestBody Map data) {
        List resultList = new ArrayList();
        try {
            SQLServerDriver sqlServerDriver = new SQLServerDriver();
            DriverManager.registerDriver(sqlServerDriver);
            Connection connection = DriverManager.getConnection(sqlserverUrl, sqlserverUser, sqlserverPwd);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(URLDecoder.decode(data.get("sql").toString()));
            ResultSetMetaData md = resultSet.getMetaData(); //获得结果集结构信息,元数据
            int columnCount = md.getColumnCount();   //获得列数

            while (resultSet.next()) {
                Map<String,Object> rowData = new HashMap<String,Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), resultSet.getObject(i));
                }
                resultList.add(rowData);
            }

            resultSet.close();
            statement.close();
            connection.close();
            DriverManager.deregisterDriver(sqlServerDriver);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

}
