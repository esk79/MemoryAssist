<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">
    <div class="well">
        <form action="/search" method="get" role="form">
            <div class="input-group">
                <input type="search" class="form-control" name="query" placeholder="Enter search terms">
                  <span class="input-group-btn">
                    <button type="submit" class="btn btn-secondary" type="button"><span class="glyphicon glyphicon-search"></span></button>
                  </span>
            </div>
        </form>
    </div>

    <div class="well">
        <#if results??>
            <#list results as result>
                <div class="jumbotron search-result-jumbo">
                    <a href="search/${result.uid}"><h2>${result.title}</h2></a>
                    <p>${result.markdownPreview}</p>
                </div>
            </#list>
        </#if>
    </div>

</div>


</div>
<!-- /.container -->
</@layout.master>

