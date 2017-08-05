package controllers;

import annotations.IndexDirectoryString;
import com.google.inject.Inject;
import models.Resource;
import searchengine.Searcher;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import utils.MarkdownProcessor;
import utils.RouteUtils;
import utils.RouteWrapper;

import java.util.List;

import static spark.Spark.*;

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
            post("", routeWrapper.templateWrapper(this::searchResultsTemplate), new FreeMarkerEngine());
            get("/:uid", routeWrapper.templateWrapper(this::displaySingleSearchResultTemplate), new FreeMarkerEngine());
        });
    }

    ModelAndView searchResultsTemplate(Request request, Response response) throws Exception {
        routeUtils.forceAuthentication(request);
        Searcher searcher = new Searcher(indexDirectoryString);

        String searchTerms = request.queryParams("search");
        List<Resource> resultingResourceList = searcher.search(searchTerms);

        return routeUtils.modelAndView(request, "searchresult.ftl")
                .add("results", resultingResourceList)
                .get();
    }

    ModelAndView displaySingleSearchResultTemplate(Request request, Response response) throws Exception {
        routeUtils.forceAuthentication(request);
        //TODO: really wish I didnt have to create a new searcher here
        Searcher searcher = new Searcher(indexDirectoryString);

        String uid = request.params(":uid");

        Resource resource = searcher.getResourceByUID(uid);

        MarkdownProcessor markdownProcessor = new MarkdownProcessor();
        String html = markdownProcessor.convertMarkdownToHTML(resource.getMarkdown());
        String title = resource.getTitle();
        return routeUtils.modelAndView(request, "viewresource.ftl")
                .add("title", title)
                .add("html", html)
                .add("uid", uid)
                .get();
    }
}
