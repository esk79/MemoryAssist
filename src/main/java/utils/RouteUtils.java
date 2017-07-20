package utils;

import spark.Request;
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

    public boolean userIsLoggedIn(Request request)
            throws SQLException {
//        Boolean loggedIn = request.session().attribute("loggedIn");
        //TODO:
        return false;
    }

}
