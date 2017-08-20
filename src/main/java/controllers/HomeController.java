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
import static spark.Spark.internalServerError;


public class HomeController extends AbstractController {
    private static final Log LOGGER = Log.forClass(HomeController.class);

    private final RouteUtils routeUtils;

    @Inject
    private HomeController(RouteUtils routeUtils) {
        this.routeUtils = routeUtils;
    }

    // Basic route controller to serve homepage
    public void init() {
        RouteWrapper routeWrapper = new RouteWrapper();
        get("/", routeWrapper.templateWrapper(this::homePage), new FreeMarkerEngine());

        get("/404", routeWrapper.templateWrapper(this::_404Page), new FreeMarkerEngine());
        get("/500", routeWrapper.templateWrapper(this::_500Page), new FreeMarkerEngine());


        notFound((request, response) -> {
            LOGGER.info("Page %s not found", request.uri());
            response.redirect("/404");
            return "Redirected";
        });

        internalServerError((request, response) -> {
            LOGGER.info("Internal server error");
            response.redirect("/500");
            return "Redirected";
        });

    }

    ModelAndView homePage(Request request, Response response) throws Exception {
        return routeUtils.modelAndView(request, "index.ftl").get();
    }

    ModelAndView _404Page(Request request, Response response) throws Exception {
        return routeUtils.modelAndView(request, "404.ftl").get();
    }

    ModelAndView _500Page(Request request, Response response) throws Exception {
        return routeUtils.modelAndView(request, "500.ftl").get();
    }

}
