package controllers;

import annotations.IndexDirectoryString;
import com.google.inject.Inject;
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

import java.io.IOException;
import java.sql.SQLException;
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

    @Inject
    public ResourceController(RouteUtils routeUtils, Indexer indexer, @IndexDirectoryString String indexDirectoryString) {
        this.routeUtils = routeUtils;
        this.indexer = indexer;
        this.indexDirectoryString = indexDirectoryString;
    }

    @Override
    public void init() {
        RouteWrapper routeWrapper = new RouteWrapper();
        path("/add", () -> {
            get("", routeWrapper.templateWrapper(this::addResourceTemplate), new FreeMarkerEngine());
            post("", "application/json", routeUtils.route(this::addResourceOnPost));
        });
        path("/edit", () -> {
            get("/:uid", routeWrapper.templateWrapper(this::editResourceTemplate), new FreeMarkerEngine());
        });
        path("/delete", () -> {
            get("/:uid", "application/json", routeUtils.route(this::deleteResource));
        });
    }

    ModelAndView addResourceTemplate(Request request, Response response) throws Exception {
        routeUtils.forceAuthentication(request);
        return routeUtils.modelAndView(request, "addresource.ftl").get();
    }

    ModelAndView editResourceTemplate(Request request, Response response) throws Exception {
        routeUtils.forceAuthentication(request);
        Searcher searcher = new Searcher(indexDirectoryString);

        String uid = request.params(":uid");

        Resource resource = searcher.getResourceByUID(uid);

        return routeUtils.modelAndView(request, "addresource.ftl")
                .add("title", resource.getTitle())
                .add("markdown", resource.getMarkdown())
                .add("uid", uid)
                .get();
    }

    //TODO: refactor
    String addResourceOnPost(Request request, Response response) throws RouteUtils.InvalidParamException, RouteUtils.NotAuthenticatedException, SQLException {
        routeUtils.forceAuthentication(request);
        //TODO: make sure title and markdown are both present

        Optional<Resource> optionalResource = getResourceFromRequest(request);

        if (!optionalResource.isPresent()) {
            RouteUtils.errorMessage(request, "Please add both title and markdown body");
            response.redirect("/add");
            return "error";
        }

        Resource resource = optionalResource.get();

        if (resource.isUpdate()) {
            indexer.updateResource(resource);
            RouteUtils.successMessage(request, "Resource updated!");
            response.redirect("/add");
            return "ok";
        }

        try {
            indexer.addNewResource(resource);
        } catch (IOException e) {
            RouteUtils.errorMessage(request, "Unable to add to index. Please try again later.");
            response.redirect("/add");
            return "error";
        }

        RouteUtils.successMessage(request, "Resource saved!");
        response.redirect("/add");
        return "ok";
    }

    String deleteResource(Request request, Response response) throws RouteUtils.NotAuthenticatedException, SQLException, RouteUtils.InvalidParamException {
        routeUtils.forceAuthentication(request);

        String uid = RouteUtils.pathParam(request,":uid");
        indexer.deleteResource(uid);
        RouteUtils.successMessage(request, "Resource deleted!");
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


}
