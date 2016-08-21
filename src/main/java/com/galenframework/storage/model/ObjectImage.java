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

import java.util.Date;

public class ObjectImage {
    private Date createDate;
    private Long objectId;
    private Long imageId;
    private String hash;
    private String imagePath;
    private ImageStatus status = ImageStatus.candidate;

    public ObjectImage setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public ObjectImage setObjectId(Long objectId) {
        this.objectId = objectId;
        return this;
    }

    public Long getObjectId() {
        return objectId;
    }

    public ObjectImage setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public ObjectImage setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Long getImageId() {
        return imageId;
    }

    public ObjectImage setImageId(Long imageId) {
        this.imageId = imageId;
        return this;
    }

    public ImageStatus getStatus() {
        return status;
    }

    public ObjectImage setStatus(ImageStatus status) {
        this.status = status;
        return this;
    }
}
