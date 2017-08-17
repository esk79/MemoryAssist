<#macro master title="">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="icon" href="https://cdn3.iconfinder.com/data/icons/brain-games/1042/Brain-Games-grey.png">
    <title>Thalamus</title>

    <!------------------------------------------- CSS ------------------------------------------>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">


    <!-- index page css -->
    <link rel="stylesheet" type="text/css" href="/css/index.css">

    <!------------------------------------------- JS ------------------------------------------>

    <!-- Latest compiled and minified JQuery -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"
            integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
            crossorigin="anonymous"></script>

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>

</head>
<body>

<!-- Fixed navbar -->
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="https://thalamus.me">Thalamus</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="/add">Add</a></li>
            </ul>
            <ul class="nav navbar-nav">
                <li><a href="/index">Recreate Index</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</nav>
<div class="container" style="padding-top: 10px;">
    <#if success??>
        <div class="alert alert-success">
            <strong>Success!</strong> ${success}
        </div>
    </#if>
    <#if alert??>
        <div class="alert alert-warning">
            <strong>Alert!</strong> ${alert}
        </div>
    </#if>
    <#if error??>
        <div class="alert alert-danger">
            <strong>Error!</strong> ${error}
        </div>
    </#if>
</div>

    <#nested/>

</body>
</html>
</#macro>