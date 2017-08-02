package controllers;

import com.google.inject.Inject;
import dao.AuthenticatorAccess;
import models.Authenticator;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.RouteUtils;

import java.util.Optional;

import static spark.Spark.*;

/**
 * Created by EvanKing on 7/31/17.
 */
public class AuthenticationController extends AbstractController {

    private final RouteUtils routeUtils;
    private final AuthenticatorAccess authenticatorAccess;

    @Inject
    private AuthenticationController(RouteUtils routeUtils, AuthenticatorAccess authenticatorAccess) {
        this.routeUtils = routeUtils;
        this.authenticatorAccess = authenticatorAccess;
    }

    @Override
    public void init() {
        path("/authenticate", () -> {
            get("", routeUtils.template("authenticate.ftl"), new FreeMarkerEngine());
            post("", routeUtils.route(this::authenticate));
        });
    }

    String authenticate(Request request, Response response) throws Exception {
        String password = RouteUtils.queryParam(request, "password");
        Optional<Authenticator> authenticatorOptional = authenticatorAccess.getAuthenticator();

        if (!authenticatorOptional.isPresent()){
            //TODO: Authenticator is not present error handling
        }

        Authenticator authenticator = authenticatorOptional.get();

        if (authenticator.authenticate(password)){
            //Login because password was correct
            request.session(true).attribute("authenticated", true);
            response.redirect("/");
            return "redirected";
        }

        response.redirect("/authenticate");
        return "redirected";
    }

}
