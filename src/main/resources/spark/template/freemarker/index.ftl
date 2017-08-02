<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <form class="form-inline global-input" role="form">
        <div class="form-group">
            <label class="sr-only" for="">Enter search terms</label>
            <input type="search" class="form-control" id="k" name="k" placeholder="Enter search terms">
            <input id="cn" name="cn" type="hidden" value="false"/>
        </div>
        <button type="submit" id="s" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>

        </button>
    </form>

</div>


</div>
<!-- /.container -->
</@layout.master>

