<#import "master.ftl" as layout />

<@layout.master>
<!-- Page Content -->
<div class="container">

    <form action="/add" method="post" role="form" id="resource-form">
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
    </form>

</div>

</div>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
<script src="/js/MarkdownEditor.js"></script>

<!-- /.container -->
</@layout.master>

