package controllers;

import annotations.IndexDirectoryString;
import com.google.inject.Inject;
import dao.ResourceAccess;
import models.Resource;
import searchengine.Indexer;
import searchengine.Searcher;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.Log;
import utils.RouteUtils;
import utils.RouteWrapper;

import java.util.List;
import java.util.Optional;

import static spark.Spark.*;

/**
 * Created by EvanKing on 8/1/17.
 */
public class ResourceController extends AbstractController {
    private static final Log LOGGER = Log.forClass(ResourceController.class);

    private final RouteUtils routeUtils;
    private final Indexer indexer;
    private final String indexDirectoryString;
    private final ResourceAccess resourceAccess;

    @Inject
    public ResourceController(RouteUtils routeUtils, Indexer indexer, ResourceAccess resourceAccess, @IndexDirectoryString String indexDirectoryString) {
        this.routeUtils = routeUtils;
        this.indexer = indexer;
        this.resourceAccess = resourceAccess;
        this.indexDirectoryString = indexDirectoryString;
    }

    @Override
    public void init() {
        RouteWrapper routeWrapper = new RouteWrapper();
        path("/add", () -> {
            get("", routeWrapper.templateWrapper(this::addResourceTemplate), new FreeMarkerEngine());
            post("", "application/json", routeWrapper.routeWrapper(this::addResourceHandler));
        });
        path("/edit", () -> {
            get("/:uid", routeWrapper.templateWrapper(this::editResourceTemplate), new FreeMarkerEngine());
        });
        path("/delete", () -> {
            get("/:uid", "application/json", routeWrapper.routeWrapper(this::deleteResourceHandler));
        });

        get("/index", "application/json", routeWrapper.routeWrapper(this::recreateIndex));

    }

    ModelAndView addResourceTemplate(Request request, Response response) throws Exception {
        return routeUtils.modelAndView(request, "addresource.ftl").get();
    }

    ModelAndView editResourceTemplate(Request request, Response response) throws Exception {
        Searcher searcher = new Searcher(indexDirectoryString);

        String uid = routeUtils.pathParam(request, ":uid");

        Resource resource = searcher.getResourceByUID(uid);

        return routeUtils.modelAndView(request, "addresource.ftl")
                .add("title", resource.getTitle())
                .add("markdown", resource.getMarkdown())
                .add("uid", uid)
                .get();
    }

    String addResourceHandler(Request request, Response response) {

        Optional<Resource> optionalResource = getResourceFromRequest(request);

        if (!optionalResource.isPresent()) {
            RouteUtils.errorMessage(request, "Please add both title and markdown body");
            RouteUtils.redirectTo(response, "/add");
            return "redirected";
        }

        Resource resource = optionalResource.get();

        boolean success = resource.isUpdate() ? updateResource(request, response, resource) :  addResource(request, response, resource);
        if (!success) return "error";

        RouteUtils.successMessage(request, "Resource saved!");
        response.redirect("/add");
        return "ok";
    }

    String deleteResourceHandler(Request request, Response response) throws RouteUtils.InvalidParamException {

        String uid = RouteUtils.pathParam(request, ":uid");

        boolean success = deleteResource(request, response, uid);
        if (!success) return "error";

        RouteUtils.successMessage(request, "Resource deleted!");
        response.redirect("/");
        return "ok";
    }

    String recreateIndex(Request request, Response response) throws RouteUtils.InvalidParamException {
        try {
            indexer.deleteAll();
            List<Resource> resourcesFromDB = resourceAccess.getAllResources();
            indexer.createIndex(resourcesFromDB);
        } catch (Exception e) {
            LOGGER.severe("Error recreating index: %s ", e.getMessage());
            RouteUtils.errorMessage(request, "Unable to recreate Index.");
            RouteUtils.redirectTo(response, "/");
            return "redirected";
        }
        RouteUtils.successMessage(request, "Index recreated from Database!");
        response.redirect("/");
        return "ok";
    }

    Optional<Resource> getResourceFromRequest(Request request) {
        String title;
        String markdown;
        try {
            title = RouteUtils.queryParam(request, "title");
            markdown = RouteUtils.queryParam(request, "resource");
        } catch (RouteUtils.InvalidParamException e) {
            LOGGER.severe("Error getting param: %s ", e.getMessage());
            return Optional.empty();
        }

        if (RouteUtils.queryParamExists(request, "uid")) {
            String uid = null;
            try {
                uid = RouteUtils.queryParam(request, "uid");
            } catch (RouteUtils.InvalidParamException e) {
                LOGGER.severe("Error getting param: %s ", e.getMessage());
            }
            return Optional.of(new Resource(title, markdown, uid));
        }
        return Optional.of(new Resource(title, markdown));
    }

    boolean addResource(Request request, Response response, Resource resource) {
        try {
            indexer.addNewResource(resource);
            resourceAccess.insertResource(resource);
        } catch (Exception e) {
            LOGGER.severe("[-] Error: %s", e.getMessage());
            RouteUtils.errorMessage(request, "Unable to add to index. Please try again later.");
            response.redirect("/add");
            return false;
        }
        return true;
    }

    boolean updateResource(Request request, Response response, Resource resource) {
        try {
            indexer.updateResource(resource);
            resourceAccess.updateResource(resource);
        } catch (Exception e) {
            LOGGER.severe("[-] Error: %s", e.getMessage());
            RouteUtils.errorMessage(request, "Unable to add to index. Please try again later.");
            response.redirect("/add");
            return false;
        }
        return true;
    }

    boolean deleteResource(Request request, Response response, String uid) {
        try {
            indexer.deleteResource(uid);
            resourceAccess.deleteResource(uid);
        } catch (Exception e) {
            LOGGER.severe("[-] Error: %s", e.getMessage());
            RouteUtils.errorMessage(request, "Unable to delete resource. Please try again later.");
            response.redirect("/add");
            return false;
        }
        return true;
    }

}
