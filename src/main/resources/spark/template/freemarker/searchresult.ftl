<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <#list results as result>
        <div class="jumbotron search-result-jumbo">
            <h2>${result.title}</h2>
            <p>${result.markdown}</p>
        </div>
    </#list>

</div>


</div>
<!-- /.container -->
</@layout.master>

