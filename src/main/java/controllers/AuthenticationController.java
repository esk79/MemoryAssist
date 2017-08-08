package controllers;

import com.google.inject.Inject;
import dao.AuthenticatorAccess;
import models.Authenticator;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.Log;
import utils.RouteUtils;
import utils.RouteWrapper;

import java.util.Optional;

import static spark.Spark.*;

/**
 * Created by EvanKing on 7/31/17.
 */
public class AuthenticationController extends AbstractController {
    private static final Log LOGGER = Log.forClass(AuthenticationController.class);

    private final RouteUtils routeUtils;
    private final AuthenticatorAccess authenticatorAccess;

    @Inject
    private AuthenticationController(RouteUtils routeUtils, AuthenticatorAccess authenticatorAccess) {
        this.routeUtils = routeUtils;
        this.authenticatorAccess = authenticatorAccess;
    }

    @Override
    public void init() {
        RouteWrapper routeWrapper = new RouteWrapper();

        path("/authenticate", () -> {
            get("", routeUtils.template("authenticate.ftl"), new FreeMarkerEngine());
            // Not using routeWrapper below because don't want to force authenticated for authentication page
            post("", routeUtils.route(this::authenticate));
        });
    }

    String authenticate(Request request, Response response) throws Exception {
        String password = RouteUtils.queryParam(request, "password");
        Optional<Authenticator> authenticatorOptional = authenticatorAccess.getAuthenticator();

        if (!authenticatorOptional.isPresent()) {
            return RouteUtils.errorRedirect(response, "/authenticate", "Unable to create authenticator from DB.");
        }

        Authenticator authenticator = authenticatorOptional.get();

        if (authenticator.authenticate(password)) {
            //Login because password was correct
            request.session(true).attribute("authenticated", true);
            response.redirect("/");
            return "redirected";
        }

        return RouteUtils.errorRedirect(response, "/authenticate", "Incorrect authentication attempt of: " + password);
    }

}
