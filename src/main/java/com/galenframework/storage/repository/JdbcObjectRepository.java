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

import com.galenframework.storage.model.ImageStatus;
import com.galenframework.storage.model.ObjectImage;
import com.galenframework.storage.model.PageObject;
import com.jolbox.bonecp.BoneCP;

import java.util.List;
import java.util.Optional;

public class JdbcObjectRepository extends JdbcRepository implements ObjectRepository {

    private ResultSetMapper.RSFunction<PageObject> objectMapper = (rs) -> new PageObject()
        .setPageId(rs.getLong("page_id"))
        .setObjectId(rs.getLong("object_id"))
        .setName(rs.getString("name"));

    private ResultSetMapper.RSFunction<ObjectImage> objectImageMapper = (rs) -> new ObjectImage()
        .setObjectId(rs.getLong("object_id"))
        .setCreateDate(rs.getDate("created_date"))
        .setHash(rs.getString("hash"))
        .setImageId(rs.getLong("object_image_id"))
        .setImagePath(rs.getString("image_path"))
        .setStatus(ImageStatus.valueOf(rs.getString("status")));

    public JdbcObjectRepository(BoneCP connectionPool) {
        super(connectionPool);
    }

    @Override
    public Long createObjectImage(ObjectImage objectImage) {
        return insert("insert into object_images (object_id, image_path, created_date, hash, status) values (?, ?, ?, ?, ?)",
            objectImage.getObjectId(),
            objectImage.getImagePath(),
            objectImage.getCreateDate(),
            objectImage.getHash(),
            objectImage.getStatus().toString());
    }

    @Override
    public Optional<PageObject> findObject(Long pageId, String elementName) {
        return query("select * from objects where page_id = ? and name = ?", pageId, elementName).single(objectMapper);
    }

    @Override
    public Long createObject(PageObject pageObject) {
        return insert("insert into objects (page_id, name) values (?, ?)",
            pageObject.getPageId(),
            pageObject.getName());
    }

    @Override
    public List<ObjectImage> findAllObjectImages(Long objectId) {
        return query("select * from object_images where object_id = ?", objectId).list(objectImageMapper);
    }

    @Override
    public Optional<ObjectImage> findObjectImageById(Long imageId) {
        return query("select * from object_images where object_image_id = ?", imageId).single(objectImageMapper);
    }
}
