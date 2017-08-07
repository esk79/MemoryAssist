<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <div class="jumbotron search-result-jumbo">
        <h1>
            <span>${title}</span>

        </h1>
    ${html}
        <div class="edit-button-group">
            <a class='pull-right edit-button' href="/edit/${uid}"><span class="glyphicon glyphicon-edit"></span></a>
            <a class='pull-right' href="/delete/${uid}"><span class="glyphicon glyphicon-trash"></span></a>
        </div>
    </div>

</div>


</div>
<!-- /.container -->
</@layout.master>

