<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
</head>
<body>
<div id="errorDisplay"></div>



<script>
    // 使用 JavaScript 获取后端传递的错误信息并显示在页面上
    var error = "<%= session.getAttribute("error") %>";
    document.getElementById('errorDisplay').innerText = "Error: " + error;
</script>
</body>
</html>