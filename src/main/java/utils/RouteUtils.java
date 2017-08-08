package utils;

import spark.*;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by EvanKing on 7/19/17.
 */
public class RouteUtils {

    public TemplateViewRoute template(String templatePath) {
        return (request, response) -> modelAndView(request, templatePath).get();
    }

    public MapModelAndView modelAndView(Request request, String viewName)
            throws SQLException {

        Session session = request.session();
        MapModelAndView mapModelAndView = new MapModelAndView(viewName)
                .addIfNonNull("error", session.attribute("error"))
                .addIfNonNull("alert", session.attribute("alert"))
                .addIfNonNull("success", session.attribute("success"));
        session.removeAttribute("error");
        session.removeAttribute("alert");
        session.removeAttribute("success");
        return mapModelAndView;
    }

    public Route route(Route route) {
        return (request, response) -> {
            try {
                return route.handle(request, response);
            } catch (NotAuthenticatedException e) {
                response.status(403);
                return "";
            } catch (InvalidParamException e) {
                response.status(400);
                return e.getMessage();
            }
        };
    }

    public static boolean queryParamExists(Request request, String paramName) {
        return request.queryParams().contains(paramName);
    }

    public static String queryParam(Request request, String paramName)
            throws InvalidParamException {
        String value = request.queryParams(paramName);
        if (value == null || value.isEmpty()) {
            String msg = String.format("Parameter %s expected, but not found", paramName);
            throw new InvalidParamException(msg);
        }
        return value;
    }

    public static String pathParam(Request request, String paramName)
            throws InvalidParamException {
        String value = request.params(paramName);
        if (value == null || value.isEmpty()) {
            String msg = String.format("Parameter %s expected, but not found", paramName);
            throw new InvalidParamException(msg);
        }
        return value;
    }

    public boolean userAuthenticated(Request request)
            throws SQLException {
        Boolean isAuthenticated = request.session().attribute("authenticated");
        if (isAuthenticated != null && isAuthenticated) {
            return true;
        }
        return false;
    }

    public void forceAuthentication(Request request)
            throws NotAuthenticatedException, SQLException {
        if (!userAuthenticated(request)) {
            throw new NotAuthenticatedException();
        }
    }

    public static void successMessage(Request request, String message) {
        request.session().attribute("success", message);
    }

    public static void alertMessage(Request request, String message) {
        request.session().attribute("alert", message);
    }

    public static void errorMessage(Request request, String message) {
        request.session().attribute("error", message);
    }


    public static ModelAndView redirectTo(Response response, String path) {
        response.redirect(path);
        // return whatever, will be overridden by the redirect
        return new ModelAndView(new HashMap<String, Object>(), "");
    }

    public static class NotAuthenticatedException extends Exception {
    }

    public static class InvalidParamException extends Exception {
        public InvalidParamException(String message) {
            super(message);
        }
    }

}
