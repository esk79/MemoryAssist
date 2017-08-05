<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <div class="jumbotron search-result-jumbo">
        <h1>
            <span>${title}</span>
            <a class='btn btn-primary pull-right' href="/edit/${uid}"><span class="glyphicon glyphicon-edit"></span></a>
        </h1>
    ${html}
    </div>

</div>


</div>
<!-- /.container -->
</@layout.master>

