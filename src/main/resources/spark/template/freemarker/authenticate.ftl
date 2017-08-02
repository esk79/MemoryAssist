<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <form action="/authenticate" method="post" class="form-inline global-input" role="form">
        <div class="form-group">
            <input type="password" class="form-control" id="k" name="password" placeholder="Password">
        </div>
        <button type="submit" id="s" class="btn btn-default"><span class="glyphicon glyphicon-lock"></span>
        </button>

    </form>

</div>


</div>
<!-- /.container -->
</@layout.master>

