package controllers;

import com.google.inject.Inject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.Log;
import utils.RouteUtils;
import utils.RouteWrapper;

import static spark.Spark.get;
import static spark.Spark.notFound;


public class IndexController extends AbstractController {
    private static final Log LOGGER = Log.forClass(IndexController.class);

    private final RouteUtils routeUtils;

    @Inject
    private IndexController(RouteUtils routeUtils) {
        this.routeUtils = routeUtils;
    }

    // Basic route controller to serve homepage
    public void init() {
        RouteWrapper routeWrapper = new RouteWrapper();
        get("/", routeWrapper.templateWrapper(this::indexPage), new FreeMarkerEngine());

        get("/404", routeWrapper.templateWrapper(this::_404Page), new FreeMarkerEngine());

        notFound((request, response) -> {
            LOGGER.info("Page %s not found", request.uri());
            response.redirect("/404");
            return "Redirected";
        });

    }

    ModelAndView indexPage(Request request, Response response) throws Exception {
        return routeUtils.modelAndView(request, "index.ftl").get();
    }

    ModelAndView _404Page(Request request, Response response) throws Exception {
        return routeUtils.modelAndView(request, "404.ftl").get();
    }

}
