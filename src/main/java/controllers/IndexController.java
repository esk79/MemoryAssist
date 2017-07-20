package controllers;

import com.google.inject.Inject;
import spark.template.freemarker.FreeMarkerEngine;
import utils.RouteUtils;

import static spark.Spark.get;
import static spark.Spark.notFound;


public class IndexController extends AbstractController {

    private final RouteUtils routeUtils;

    @Inject
    private IndexController(RouteUtils routeUtils) {
        this.routeUtils = routeUtils;
    }

    // Basic route controller to serve homepage
    public void init() {
        get("/", routeUtils.template("index.ftl"), new FreeMarkerEngine());

        get("/404", routeUtils.template("404.ftl"), new FreeMarkerEngine());

        notFound((request, response) -> {
            response.redirect("/404");
            return "Redirected";
        });
    }

}
