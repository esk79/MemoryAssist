package utils;

import spark.*;

public final class RouteWrapper {

    private final RouteUtils routeUtils = new RouteUtils();


    @FunctionalInterface
    public interface LoggedRoute {
        Object handle(Request request, Response response) throws Exception;
    }

    @FunctionalInterface
    public interface LoggedTemplateViewRoute {
        ModelAndView handle(Request request, Response response) throws Exception;
    }

    public Route routeWrapper(LoggedRoute route) {
        return (request, response) -> {
            try {
                routeUtils.forceAuthentication(request);
                return route.handle(request, response);
            } catch (RouteUtils.NotAuthenticatedException e) {
                response.status(403);
                return "";
            } catch (RouteUtils.InvalidParamException e) {
                response.status(400);
                return e.getMessage();
            }
        };
    }

    public TemplateViewRoute templateWrapper(LoggedTemplateViewRoute route) {
        return (request, response) -> {
            try {
                routeUtils.forceAuthentication(request);
                return route.handle(request, response);
            } catch (RouteUtils.NotAuthenticatedException e) {
                return RouteUtils.redirectTo(response, "/authenticate");
            } catch (RouteUtils.InvalidParamException e) {
                response.status(400);
                response.body("Invalid Parameters.");
                return null;
            }
        };
    }
}
