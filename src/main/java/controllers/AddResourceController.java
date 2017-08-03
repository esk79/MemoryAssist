package controllers;

import com.google.inject.Inject;
import dao.ResourceAccess;
import models.Resource;
import searchengine.Indexer;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.RouteUtils;
import utils.RouteWrapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static spark.Spark.*;

/**
 * Created by EvanKing on 8/1/17.
 */
public class AddResourceController extends AbstractController {

    private final RouteUtils routeUtils;
    private final ResourceAccess resourceAccess;
    private final Indexer indexer;

    @Inject
    public AddResourceController(RouteUtils routeUtils, ResourceAccess resourceAccess, Indexer indexer) {
        this.routeUtils = routeUtils;
        this.resourceAccess = resourceAccess;
        this.indexer = indexer;
    }

    @Override
    public void init() {
        RouteWrapper routeWrapper = new RouteWrapper();
        path("/add", () -> {
            get("", routeWrapper.templateWrapper(this::addResourceTemplate), new FreeMarkerEngine());
            post("", "application/json", routeUtils.route(this::addResourceOnPost));
        });
    }

    ModelAndView addResourceTemplate(Request request, Response response) throws Exception {
        routeUtils.forceAuthentication(request);
        return routeUtils.modelAndView(request, "addresource.ftl").get();
    }

    //TODO: refactor
    String addResourceOnPost(Request request, Response response) {

        Optional<Resource> optionalResource = getResourceFromRequest(request);

        if (!optionalResource.isPresent()){
            RouteUtils.errorMessage(request, "Please add both title and markdown body");
            response.redirect("/add");
            return "error";
        }

        Resource resource = optionalResource.get();

        try {
            resourceAccess.insertResource(optionalResource.get());
            indexer.addNewResource(resource);
        } catch (SQLException e) {
            RouteUtils.errorMessage(request, "Unable to add to database. Please try again later.");
            response.redirect("/add");
            return "error";
        } catch (IOException e) {
            RouteUtils.errorMessage(request, "Unable to add to index. Please try again later.");
            response.redirect("/add");
            return "error";
        }

        RouteUtils.successMessage(request, "Resource saved!");
        response.redirect("/add");
        return "ok";
    }

    Optional<Resource> getResourceFromRequest(Request request){
        String title;
        String markdown;
        try {
            title = RouteUtils.queryParam(request, "title");
            markdown = RouteUtils.queryParam(request, "resource");
        } catch (RouteUtils.InvalidParamException e) {
            //TODO: LOG
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(new Resource(title, markdown));

    }



}
