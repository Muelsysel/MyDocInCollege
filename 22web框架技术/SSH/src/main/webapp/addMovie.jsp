<%@ page import="java.util.List" %>
<%@ page import="SSH.entity.Type" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>添加电影</title>
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
            height: 100vh; /* 侧边栏高度与视口高度一致 */
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
            background-color: #f4f4f4; /* 轻微灰色背景 */
        }

        #upload-news-form {
            margin: 20px auto;
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* 更细微的阴影 */
            width: 50%;
            box-sizing: border-box;
            border-radius: 8px; /* 圆角边框 */
        }

        #upload-news-form div {
            margin-bottom: 15px; /* 为每个输入区域添加底部边距 */
        }

        #upload-news-form label {
            display: block; /* 将标签设置为块级元素 */
            margin-bottom: 5px; /* 在标签和输入框之间添加一些空间 */
        }

        #upload-news-form input[type="text"],
        #upload-news-form input[type="number"],
        #upload-news-form textarea {
            width: 100%; /* 输入框宽度设置为100% */
            padding: 15px;
            border: 2px solid #e0e0e0; /* 更粗的边框 */
            border-radius: 8px; /* 圆角边框 */
            box-sizing: border-box;
            transition: border-color 0.3s; /* 边框颜色过渡效果 */
        }

        #upload-news-form input[type="text"]:focus,
        #upload-news-form input[type="number"]:focus,
        #upload-news-form textarea:focus {
            border-color: #a0a0a0; /* 聚焦时边框颜色变化 */
        }

        #upload-news-form textarea {
            height: 150px;
        }

        #upload-news-form button {
            width: 30%;
            padding: 10px 0;
            background: #87CEFA; /* 按钮渐变色 */
            color: white; /* 文字颜色 */
            cursor: pointer;
            transition: background-color 0.3s;
            border-radius: 20px; /* 圆形按钮 */
            border: none; /* 无边框 */
            box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* 按钮阴影 */
            display: block;
            margin: 20px auto;
            box-sizing: border-box;
        }

        #upload-news-form button:hover {
            background: #00CED1; /* 鼠标悬停时按钮颜色变化 */
        }
        .styled-select {
            background: #fff;
            border: 1px solid #ccc;
            border-radius: 4px;
            display: inline-block;
            font-size: 16px;
            padding: 5px 10px;
            width: 200px; /* 您可以根据需要调整宽度 */
        }

        .styled-select:hover {
            border-color: #aaa;
        }

        .styled-select:focus {
            border-color: #66afe9;
            outline: 0;
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
<div id="content">
    <!-- 功能内容将在这里显示 -->
    <div id="upload-news-form">
        <h1>增加电影</h1>
        <form id="addFilmForm" action="javascript:void(0);">
            <div>
                <input type="text" id="name" name="name" placeholder="电影名称">
            </div>
            <div>
                <input type="text" id="director" name="director" placeholder="导演">
            </div>
            <div>
                请选择电影类型:
                <select id="type" name="type" class="styled-select">
                    <!-- 使用 JSP 脚本标签获取会话中存储的类型列表 -->
                    <s:iterator value="%{#session.Types}">
                        <option value="<s:property value='type'/>"><s:property value='type'/></option>
                    </s:iterator>
                </select>
            </div>

            <div>
                <input type="text" id="language" name="language" placeholder="语言">
            </div>
            <div>
                <input type="number" id="score" name="score" step="any" placeholder="评分">
            </div>
            <div>
                <textarea id="description" name="description" rows="4" cols="50" placeholder="描述"></textarea>
            </div>
            <div>
                <input type="text" id="video" name="video" placeholder="视频地址  非必填">
            </div>
            <div>
                图片:<input type="file" id="fileInput" accept="image/*">
                <button class="button" onclick="uploadAndSave()">上传图片并提交电影</button>
            </div>
        </form>
    </div>

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

        var name = document.getElementById("name").value;
        var director = document.getElementById("director").value;
        var type = document.getElementById("type").value;
        var language = document.getElementById("language").value;
        var score = document.getElementById("score").value;
        var description = document.getElementById("description").value;
        var video = document.getElementById("video").value;

        // 上传图片
        if (file!==null&&file!==undefined&&file!==""){
            fetch("upload.action", {
                method: "POST",
                body: formData
            }).then(() => {
                if (video!==""&&video!==undefined&&video!==null){
                    window.location.href = "addFilm.action?name=" + name + "&director=" + director + "&type=" + type + "&language=" + language + "&score=" + score + "&description=" + description+"&video="+video;
                }else{
                    window.location.href = "addFilm.action?name=" + name + "&director=" + director + "&type=" + type + "&language=" + language + "&score=" + score + "&description=" + description;
                }
                // 图片上传成功后，再上传电影信息
            })
        }else if (name!=="" && director!=="" && type!=="" && language!=="" && score!=="" && description!==""){
            if (video!==""&&video!==undefined&&video!==null){
                window.location.href = "addFilm.action?name=" + name + "&director=" + director + "&type=" + type + "&language=" + language + "&score=" + score + "&description=" + description+"&video="+video;
            }else{
                window.location.href = "addFilm.action?name=" + name + "&director=" + director + "&type=" + type + "&language=" + language + "&score=" + score + "&description=" + description;
            }
        }else {
            alert("请填写完整信息");
        }

    }

</script>
</html>