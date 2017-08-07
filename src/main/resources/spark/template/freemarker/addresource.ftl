<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <form action="/add" method="post" role="form">
        <div class="form-group">
            <#if title??>
                <input class="form-control" name="title" placeholder="Title" value="${title}">
            <#else>
                <input class="form-control" name="title" placeholder="Title">
            </#if>
        </div>
        <div class="form-group">
            <#if markdown??>
                <textarea id="markdown-editor" name="resource">${markdown}</textarea>
            <#else>
                <textarea id="markdown-editor" name="resource"></textarea>
            </#if>
        </div>
        <#if uid??>
            <input type="hidden" name="uid" value=${uid}>
        </#if>
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
//        toolbar: [{
//            name: "bold",
//            action: SimpleMDE.toggleBold,
//            className: "fa fa-trash",
//            title: "Bold (Ctrl+B)",
//        }]
    });
</script>
<!-- /.container -->
</@layout.master>

