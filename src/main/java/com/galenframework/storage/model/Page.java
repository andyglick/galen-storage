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
package com.galenframework.storage.model;

public class Page {
    private String pageName;
    private Long projectId;
    private Long pageId;

    public Page() {
    }

    public Page(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public Page setPageName(String pageName) {
        this.pageName = pageName;
        return this;
    }

    public Page setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Page setPageId(Long pageId) {
        this.pageId = pageId;
        return this;
    }

    public Long getPageId() {
        return pageId;
    }
}
