<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">
    <div class="well">
        <form action="/search" method="post" class="form-inline" role="form">
            <div class="form-group">
                <input type="search" class="form-control" name="search" placeholder="Enter search terms">
            </div>
            <button type="submit" id="s" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>
            </button>
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

