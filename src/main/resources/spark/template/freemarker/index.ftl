<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <form action="/search" method="post" class="form-inline global-input" role="form">
        <div class="form-group">
            <input type="search" class="form-control" name="search" placeholder="Enter search terms">
        </div>
        <button type="submit" id="s" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>

        </button>
    </form>

</div>


</div>
<!-- /.container -->
</@layout.master>

