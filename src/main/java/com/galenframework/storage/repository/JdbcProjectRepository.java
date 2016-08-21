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
package com.galenframework.storage.repository;

import com.galenframework.storage.model.Project;
import com.jolbox.bonecp.BoneCP;

import java.util.Optional;

public class JdbcProjectRepository extends JdbcRepository implements ProjectRepository {
    public JdbcProjectRepository(BoneCP connectionPool) {
        super(connectionPool);
    }

    @Override
    public long createProject(String name) {
        return insert("insert into projects (name) values (?)", name);
    }

    @Override
    public Optional<Project> findProject(String projectName) {
        return query("select * from projects where name = ?", projectName).single(rs -> new Project()
                .setName(rs.getString("name"))
                .setProjectId(rs.getLong("project_id"))
        );
    }


}
