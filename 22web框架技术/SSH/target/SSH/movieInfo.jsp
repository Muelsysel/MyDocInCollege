<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>电影信息</title>
    <style>
        /* 添加CSS样式来创建更美观的侧边栏布局 */
        body {
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            font-family: 'Arial', sans-serif; /* 设置默认字体 */
        }
        #sidebar {
            float: left;
            width: 250px;
            /*background: #87CEFA; !* 侧边栏背景颜色 *!*/
            position: fixed; /* 将侧边栏固定在屏幕上 */
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2); /* 添加阴影效果 */
            height: 150vh; /* 侧边栏高度与视口高度一致 */
        }
        #sidebar ul {
            list-style-type: none;
            padding: 0;
            margin: 0;
        }
        #sidebar li {
            padding: 10px;
            margin-bottom: 5px;
            background: linear-gradient(to left, rgb(247, 209, 215), rgb(140, 213, 246));
            cursor: pointer;
            display: flex;
            align-items: center;
            border-radius: 5px; /* 添加圆角 */
            transition: background-color 0.3s; /* 添加背景颜色过渡效果 */
        }
        #sidebar li:hover {
            background: #B0E0E6; /* 鼠标悬停时的背景颜色 */
        }
        #sidebar .icon {
            margin-right: 10px;
            font-size: 20px; /* 调整图标大小 */
        }
        #sidebar .submenu {
            display: none; /* 默认不显示子菜单 */
            flex-direction: column; /* 子菜单垂直排列 */
            padding-left: 20px; /* 子菜单左侧内边距 */
        }
        #sidebar li:hover .submenu {
            display: flex; /* 鼠标悬停时显示子菜单 */
        }
        #sidebar .submenu li {
            background: transparent; /* 子菜单背景透明 */
            margin-bottom: 3px; /* 子菜单项间距 */
            padding-left: 10px; /* 子菜单项左侧内边距 */
        }
        #content {
            margin-left: 270px; /* 调整内容区域的左侧外边距 */
            padding: 20px;
            /*background: #F8F8F8; !* 内容区域背景颜色 *!*/

            min-height: 100vh; /* 内容区域最小高度与视口高度一致 */
        }

        body {
            font-family: 'Arial', sans-serif;
            text-align: center;
        }

        .container {
            margin: 20px auto;
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            padding: 40px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 60%;
            box-sizing: border-box;
            border-radius: 8px;
            transition: transform 0.3s ease-in-out;
            height: 150vh;
        }


        h1 {
            color: #333;
            margin-bottom: 30px;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        .input-group {
            margin-bottom: 20px;
        }

        .input-group label {
            display: block;
            margin-bottom: 5px;
            color: #666;
            font-size: 16px;
        }

        .input-group input,
        .input-group input[type='text'],
        .input-group input[type='date'],
        .input-group textarea {
            width: calc(100% - 30px);
            padding: 15px;
            margin-bottom: 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            box-sizing: border-box;
            transition: border-color 0.3s;
            background-color: #fff; /* 更改背景颜色 */
            color: #333;
        }

        .input-group input:focus,
        .input-group input[type='text']:focus,
        .input-group input[type='date']:focus,
        .input-group textarea:focus {
            outline: none;
            border-color: #a0a0a0;
            background-color: #fff;
        }

        .input-group textarea {
            height: 150px;
        }

        input[type='submit'] {
            width: 30%;
            padding: 10px 0;
            background: #87CEFA;
            color: white;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            transition: background-color 0.3s;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            display: block;
            margin: 20px auto;
            box-sizing: border-box;
        }

        input[type='submit']:hover {
            background: #00CED1;
        }

        input[type='file'], button {
            /*文字保持中心*/
            text-align: center;
            width: 100%;
            padding: 10px 0;
            background: #87CEFA;
            color: white;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            transition: background-color 0.3s;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            display: block;
            margin: 10px auto;
            box-sizing: border-box;
        }

        input[type='file']:hover, button:hover {
            background: #00CED1;
        }

        /* 移除文件选择按钮的默认样式 */
        input[type='file']::file-selector-button {
            display: none; /* 隐藏默认的文件选择按钮 */
        }

        /* 添加自定义样式的伪元素来代替默认的文件选择按钮 */
        input[type='file']:before {
            content: '上传图片:'; /* 设置按钮显示的文字 */
            display: inline-block; /* 使伪元素像块级元素一样显示 */
            padding: 5px 10px;  /*//内边距 */
            background: #87CEFA; /* 浅蓝色背景 */
            color: white; /* 文字颜色为白色 */
            border-radius: 20px; /* 边缘圆角 */
            cursor: pointer; /* 鼠标悬停时显示手指形状 */

            margin-left: 30px;/*离左侧边框距离为10px*/
            transition: background-color 0.3s; /* 背景颜色变化的过渡效果 */
            /*使大小适中*/
        }

        /* 鼠标悬停在自定义伪元素上时的样式 */
        input[type='file']:hover:before {
            background: #00CED1; /* 深青色背景 */
        }
        .left-column {
            float: left;
            width: 50%;
        }

        .right-column {
            float: right;
            width: 50%;
        }

        /* 超链接样式 */
        a.download-link {
            display: inline-block;
            padding: 10px 20px;
            background-color: #87CEFA;
            color: white;
            text-decoration: none;
            border-radius: 20px;
            transition: background-color 0.3s;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            font-weight: bold;
        }

        a.download-link:hover {
            background-color: #00CED1;
        }

        /* 图片居中样式 */
        img.centered {
            display: block;
            margin: 0 auto; /* 上下保持0，左右自动调整以居中 */
            width: 250px; /* 图片宽度 */
            height: 375px; /* 图片高度 */
        }

    </style>

    <script>

    </script>

</head>
<body>

<div id="sidebar">
    <ul>
        <li onclick="showContent('main')"><span class="icon">🏠</span>首页</li>
        <li><span class="icon">🎬</span>电影管理
            <ul class="submenu">
                <li onclick="showContent('movieShow')">电  影  展  示</li>
                <li onclick="showContent('movieManagement')">电  影  信  息</li>
                <li onclick="showContent('movieCategory')">电  影  类  别</li>
            </ul>
        </li>
        <li><span class="icon">📰</span>新闻管理
            <ul class="submenu">
                <li onclick="showContent('newsUpload')">新  闻  上  传</li>
                <li onclick="showContent('newsManagement')">新  闻  管  理</li>
            </ul>
        </li>
        <li><span class="icon">⚙</span>个人信息
            <ul class="submenu">
                <li onclick="showContent('userUpdate')">个  人  信  息</li>
                <li onclick="showContent('userPassword')">密  码  修  改</li>
                <li onclick="showContent('logout')">退  出  登  录</li>
            </ul>
        </li>
    </ul>
</div>
<div class="container">
    <h1>电影信息</h1>
    <s:if test="%{#session.Film != null}">
        <s:iterator value="%{#session.Film}">
            <div class="left-column">
                <form id="addFilmForm1" action="javascript:void(0);">
                    <!-- 左侧表单元素 -->
                    <s:hidden name="id" value="%{id}"/>
                    <s:hidden name="url" value="%{url}" disabled="true"/>
                    <div class="input-group">
                        <s:textfield label="电影名称" name="name" value="%{name}"/>
                    </div>
                    <div class="input-group">
                        <s:textfield label="导演" name="director" value="%{director}"/>
                    </div>
                    <div class="input-group">
                        <s:textfield label="评分" name="score" value="%{score}"/>
                    </div>
                    <div class="input-group">
                        <s:textfield label="类型" name="type" value="%{type}"/>
                    </div>
                    <div class="input-group">
                        <s:textfield label="语言" name="language" value="%{language}"/>
                    </div>
                    <div class="input-group">
                        <s:textarea label="简介" name="description" value="%{description}" rows="4" cols="50"/><br/>
                    </div>
                </form>
            </div>
            <div class="right-column">
                <form id="addFilmForm2" action="javascript:void(0);">
                    <div class="input-group">
                        <s:textfield label="电影地址" name="video" value="%{video}"/>
                    </div>
                    <%-- 展示图片 --%>
                    <img src="<s:property value='%{url}'/>" alt="电影图片" class="centered"> <br/>
                        <%-- 使用超链接下载视频 --%>
                    <s:a href="%{video}" target="_blank" class="download-link">点击播放或下载电影</s:a> <br>
                    <input type="file" id="fileInput" accept="image/*"><br>
                    <button onclick="uploadAndSave()">提交</button>
                </form>
            </div>
        </s:iterator>
    </s:if>
</div>
</body>
<script>
    function showContent(section) {
        // 根据名字拼接出新的jsp名字并跳转
        if(section==='logout'){
            //弹出确认框，确认后返回login界面
            if(confirm("确认退出登录？")){
                window.location.href = "logout.action";
            }
        }else{
            window.location.href = section + '.jsp';
        }


    }
    var errorMsg="<s:property value='#session.error'/>";
    if(errorMsg!==""){
        alert(errorMsg);
        //消除session中error的信息
        <%request.getSession().removeAttribute("error");%>
    }

    function uploadAndSave() {
        var file = document.getElementById("fileInput").files[0];
        var formData = new FormData();
        formData.append("file", file);

        var id = document.getElementById("id").value;
        var name = document.getElementById("name").value;
        var director = document.getElementById("director").value;
        var type = document.getElementById("type").value;
        var language = document.getElementById("language").value;
        var score = document.getElementById("score").value;
        var url = document.getElementById("url").value;
        var description = document.getElementById("description").value;
        var video = document.getElementById("video").value;

                // 上传图片
        if (file!==null&&file!==undefined&&file!==""){
            fetch("upload.action", {
                method: "POST",
                body: formData
            }).then(() => {
                if (name!=="" && director!=="" && type!=="" && language!=="" && score!=="" && description!==""&&video!=="")
                    // 图片上传成功后，再上传电影信息
                    window.location.href = "updateFilm.action?name=" + name +"&id="+ id + "&director=" + director
                        + "&type=" + type + "&language=" + language + "&score=" + score + "&description=" + description+"&video="+video;
                else alert("请填写完整信息");
            })
        }else if (name!=="" && director!=="" && type!=="" && language!=="" && score!=="" && description!==""&&url!==""&&video!==""){
            window.location.href = "updateFilm.action?name=" + name +"&id="+ id + "&director=" + director
                + "&type=" + type + "&language=" + language + "&score=" + score +"&url="+url +"&description=" + description+"&video="+video;
        }else {
            alert("请填写完整信息");
        }

    }

</script></body>
</html>

