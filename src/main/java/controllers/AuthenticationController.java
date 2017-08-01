package controllers;

import com.google.inject.Inject;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.RouteUtils;

import static spark.Spark.*;

/**
 * Created by EvanKing on 7/31/17.
 */
public class AuthenticationController extends AbstractController {

    private final RouteUtils routeUtils;

    @Inject
    private AuthenticationController(RouteUtils routeUtils) {
        this.routeUtils = routeUtils;
    }

    @Override
    public void init() {
        path("/login", () -> {
            get("", routeUtils.template("authenticate.ftl"), new FreeMarkerEngine());
            post("", routeUtils.route(this::login));
        });
    }

    String login(Request request, Response response) throws Exception {
        String password = RouteUtils.queryParam(request, "password");
        return "redirected";
    }

}
