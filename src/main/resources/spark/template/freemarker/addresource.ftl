<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <form action="/add" method="post" role="form">
        <div class="form-group">
            <input class="form-control" name="title" placeholder="Title">
        </div>
        <div class="form-group">
            <textarea id="markdown-editor" name="resource"></textarea>
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
    </form>

</div>

</div>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>

<script>
    var simplemde = new SimpleMDE({
        autofocus: true,
        autosave: {
            enabled: true,
            uniqueId: "MyUniqueID",
            delay: 1000,
        },
    });
</script>
<!-- /.container -->
</@layout.master>

