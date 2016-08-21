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

import com.galenframework.storage.model.Page;
import com.jolbox.bonecp.BoneCP;

import java.util.Optional;

public class JdbcPageRepository extends JdbcRepository implements PageRepository {
    public JdbcPageRepository(BoneCP connectionPool) {
        super(connectionPool);
    }

    @Override
    public Optional<Page> findPage(Long projectId, String pageName) {
        return query("select * from pages where project_id = ? and name = ?", projectId, pageName)
            .single(rs -> new Page()
                .setPageId(rs.getLong("page_id"))
                .setProjectId(rs.getLong("project_id"))
                .setPageName(rs.getString("name"))
        );
    }

    @Override
    public Long createPage(Page page) {
        return insert("insert into pages (project_id, name) values (?, ?)", page.getProjectId(), page.getPageName());
    }
}
