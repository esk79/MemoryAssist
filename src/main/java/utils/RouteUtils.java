package utils;

import spark.Request;
import spark.Route;
import spark.Session;
import spark.TemplateViewRoute;

import java.sql.SQLException;

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
                .add("loggedIn", userIsLoggedIn(request))
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
            } catch (NotLoggedInException e) {
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
        if (value == null) {
            String msg = String.format("Parameter %s expected, but not found", paramName);
            throw new InvalidParamException(msg);
        }
        return value;
    }

    public boolean userIsLoggedIn(Request request)
            throws SQLException {
//        Boolean loggedIn = request.session().attribute("loggedIn");
        //TODO:
        return false;
    }

    public static class NotLoggedInException extends Exception {
    }

    public static class InvalidParamException extends Exception {
        public InvalidParamException(String message) {
            super(message);
        }
    }

}
