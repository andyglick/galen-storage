/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.storage;

import com.galenframework.storage.controllers.api.PageApiController;
import com.galenframework.storage.controllers.api.ProjectApiController;
import com.galenframework.storage.repository.*;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String jdbcUrl = "jdbc:mysql://localhost/galen_storage?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUser("root");
        dataSource.setPassword("root123");

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername("root");
        config.setPassword("root123");
        BoneCP connectionPool = new BoneCP(config);

        ProjectRepository projectRepository = new JdbcProjectRepository(connectionPool);
        PageRepository pageRepository = new JdbcPageRepository(connectionPool);
        ObjectRepository objectRepository = new JdbcObjectRepository(connectionPool);
        FileStorage fileStorage = new LocalFileStorage("image_storage");

        new ProjectApiController(projectRepository);
        new PageApiController(projectRepository, pageRepository, objectRepository, fileStorage);
    }
}
