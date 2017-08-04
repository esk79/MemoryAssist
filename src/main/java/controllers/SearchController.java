package controllers;

import annotations.IndexDirectoryString;
import com.google.inject.Inject;
import models.Resource;
import searchengine.Searcher;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.RouteUtils;
import utils.RouteWrapper;

import java.util.List;

import static spark.Spark.path;
import static spark.Spark.post;

/**
 * Created by EvanKing on 8/2/17.
 */
public class SearchController  extends AbstractController {

    private final RouteUtils routeUtils;
    private final String indexDirectoryString;

    @Inject
    public SearchController(RouteUtils routeUtils, @IndexDirectoryString String indexDirectoryString) {
        this.routeUtils = routeUtils;
        this.indexDirectoryString = indexDirectoryString;
    }

    @Override
    public void init() {
        RouteWrapper routeWrapper = new RouteWrapper();
        path("/search", () -> {
            post("",  routeWrapper.templateWrapper(this::searchResultsTemplate), new FreeMarkerEngine());
        });
    }

    ModelAndView searchResultsTemplate(Request request, Response response) throws Exception {
        Searcher searcher = new Searcher(indexDirectoryString);
        routeUtils.forceAuthentication(request);

        String searchTerms = request.queryParams("search");
        List<Resource> resultingResourceList = searcher.search(searchTerms);

        return routeUtils.modelAndView(request, "searchresult.ftl")
                .add("results", resultingResourceList)
                .get();
    }
}
