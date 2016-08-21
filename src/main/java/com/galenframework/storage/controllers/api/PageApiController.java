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
package com.galenframework.storage.controllers.api;

import com.galenframework.storage.model.*;
import com.galenframework.storage.repository.ObjectRepository;
import com.galenframework.storage.repository.FileStorage;
import com.galenframework.storage.repository.PageRepository;
import com.galenframework.storage.repository.ProjectRepository;
import org.apache.commons.io.IOUtils;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static com.galenframework.storage.controllers.JsonTransformer.toJson;
import static java.util.Collections.emptyList;
import static spark.Spark.get;
import static spark.Spark.post;

public class PageApiController {
    private final ProjectRepository projectRepository;
    private final PageRepository pageRepository;
    private final ObjectRepository objectRepository;
    private final FileStorage fileStorage;

    public PageApiController(ProjectRepository projectRepository, PageRepository pageRepository,
                             ObjectRepository objectRepository, FileStorage fileStorage) {
        this.projectRepository = projectRepository;
        this.pageRepository = pageRepository;
        this.objectRepository = objectRepository;
        this.fileStorage = fileStorage;
        init();
    }

    public void init() {
        initElementImages();
        initElementImageUpload();
    }

    private void initElementImageUpload() {
        post("/api/projects/:projectName/pages/:pageName/objects/:objectName/candidate-images", (req, res) -> {
            String projectName = req.params("projectName");
            String pageName = req.params("projectName");
            String objectName = req.params("objectName");

            Page page = findOrCreatePage(obtainProjectId(projectName), pageName);
            uploadCandidateImage(page, objectName, copyImageToStorage(req));
            return null;
        }, toJson());
    }

    private void uploadCandidateImage(Page page, String objectName, FileInfo imageInfo) {
        PageObject pageObject = findOrCreatePageObject(page, objectName);
        ObjectImage objectImage = new ObjectImage();
        objectImage.setCreateDate(new Date());
        objectImage.setStatus(ImageStatus.candidate);
        objectImage.setObjectId(pageObject.getObjectId());
        objectImage.setHash(imageInfo.getHash());
        objectImage.setImagePath(imageInfo.getPath());
        objectRepository.createObjectImage(objectImage);
    }

    private PageObject findOrCreatePageObject(Page page, String elementName) {
        Optional<PageObject> pageObject = objectRepository.findObject(page.getPageId(), elementName);
        if (pageObject.isPresent()) {
            return pageObject.get();
        } else {
            PageObject po = new PageObject();
            po.setPageId(page.getPageId());
            po.setName(elementName);
            Long elementId = objectRepository.createObject(po);
            po.setObjectId(elementId);
            return po;
        }
    }

    private void initElementImages() {
        get("/api/projects/:projectName/pages/:pageName/objects/:elementName/images", (req, res) -> {
            String projectName = req.params("projectName");
            String pageName = req.params("projectName");
            String elementName = req.params("elementName");

            Long projectId = obtainProjectId(projectName);
            Optional<Page> page = pageRepository.findPage(projectId, pageName);
            if (page.isPresent()) {
                Optional<PageObject> pageObject = objectRepository.findObject(page.get().getPageId(), elementName);
                if (pageObject.isPresent()) {
                    return objectRepository.findAllObjectImages(pageObject.get().getObjectId());
                }
            }
            return emptyList();
        }, toJson());

        get("/api/projects/:projectName/pages/:pageName/objects/:elementName/images/:imageId", (req, res) -> {
            Long imageId = Long.parseLong(req.params("imageId"));
            Optional<ObjectImage> objectImage = objectRepository.findObjectImageById(imageId);
            if (objectImage.isPresent()) {
                byte[] imageBytes = fileStorage.readFile(objectImage.get().getImagePath());
                BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(imageBytes));
                ServletOutputStream os = res.raw().getOutputStream();
                IOUtils.copy(inputStream, os);
                os.flush();
                os.close();
                return os;
            } else {
                throw new RuntimeException("Can't find image");
            }
        });
    }

    private FileInfo copyImageToStorage(Request req) throws IOException, ServletException {
        if (req.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
            req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        }

        Part file = req.raw().getPart("file");
        FileInfo imageInfo = fileStorage.saveImageToStorage(file.getInputStream());
        file.delete();
        return imageInfo;
    }

    private Page findOrCreatePage(Long projectId, String pageName) {
        Optional<Page> page = pageRepository.findPage(projectId, pageName);
        if (page.isPresent()) {
            return page.get();
        } else {
            Page p = new Page(pageName);
            p.setProjectId(projectId);
            Long pageId = pageRepository.createPage(p);
            p.setPageId(pageId);
            return p;
        }
    }


    private Long obtainProjectId(String projectName) {
        Optional<Project> project = projectRepository.findProject(projectName);
        if (project.isPresent()) {
            return project.get().getProjectId();
        } else {
            throw new RuntimeException("Missing project: " + projectName);
        }
    }

}
