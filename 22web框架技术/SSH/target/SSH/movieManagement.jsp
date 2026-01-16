<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!D<%@ taglib prefix="s" uri="/struts-tags" %>
OCTYPE html>
<html>
<head>
    <title>电影列表</title>
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
            /*background: linear-gradient(to left, rgb(247, 209, 215), rgb(191, 227, 241));*/
            padding: 20px;
            position: fixed; /* 将侧边栏固定在屏幕上 */
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
            /*background: #FFFFFF;*/
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
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            margin: 0;
            padding: 0;
        }

        #news-list {
            margin: 20px;
            /*background: #F8F8F8;*/
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }

        #news-list table {
            width: 100%;
            border-collapse: collapse;
        }

        #news-list th, #news-list td {
            text-align: left;
            padding: 8px;
            border-bottom: 1px solid #ddd;
        }

        #news-list tr:hover {
            background-color: #B0E0E6;
        }

        #news-list .header {
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            color: white;
            font-size: 20px;
        }

        .button {
            display: inline-block;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
            outline: none;
            color: #fff;
            background-color: #f8a9b8;
            border: none;
            border-radius: 15px;
        }

        .button:hover {
            background-color: #f361a5;
        }

        .add-container, .search-container {
            display: inline-block; /* 使容器内联显示 */
            margin: 10px;
        }

        .search-container {
            float: right; /* 将搜索容器浮动到右侧 */
        }

        .search-container input[type="text"] {
            width: 200px; /* 输入框宽度 */
            padding: 8px 15px; /* 内边距，左右稍大 */
            font-size: 14px; /* 字体大小 */
            border: 1px solid #87CEFA; /* 边框颜色 */
            border-radius: 20px; /* 圆角边框 */
            outline: none; /* 去除焦点轮廓 */
            transition: all 0.3s; /* 平滑过渡效果 */
            margin-right: 10px; /* 与按钮间隔 */
        }

        .search-container input[type="text"]:focus {
            box-shadow: 0 0 8px rgba(135, 206, 250, 0.8); /* 聚焦时的阴影效果 */
            border-color: #B0E0E6; /* 聚焦时的边框颜色 */
        }

        .search-container input[type="submit"] {
            padding: 8px 15px; /* 按钮内边距 */
            font-size: 14px; /* 字体大小 */
            color: white; /* 字体颜色 */
            background-color: #f8a9b8; /* 背景颜色 */
            border: none; /* 无边框 */
            border-radius: 20px; /* 圆角边框 */
            cursor: pointer; /* 鼠标手势 */
            transition: background-color 0.3s; /* 背景颜色过渡效果 */
        }

        .search-container input[type="submit"]:hover {
            background-color: #f361a5;/* 鼠标悬停时的背景颜色 */
        }

    </style>
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
    <%--    <label for="newFilm"></label><input type="text" id="newFilm" placeholder="新增类型">--%>


    <!-- 功能内容将在这里显示 -->

    <div id="news-list">
        <h1>电影列表</h1>

        <div class="add-container">
            <form action="addMovie.jsp" method="get">
                <input type="submit" class="button" value="增加电影">
            </form>
        </div>

        <div class="search-container">
            <form action="getFilmByKey.action" method="get">
                <input type="text" name="name" placeholder="输入关键字">
                <input type="submit" class="button" value="搜索">
            </form>
        </div>

        <table>
            <tr class="header">
                <th>ID</th>
                <th>电影名称</th>
                <th>导演</th>
                <th>电影类型</th>
                <th>语言</th>
                <th>评分</th>
                <th>电影内容</th>
                <th width=" 200px">操作</th>
            </tr>
            <!-- 表格内容 -->
            <s:iterator value="%{#session.Films}" var="film">
                <tr>
                    <td><s:property value="#film.id" /></td>
                    <td><s:property value="#film.name" /></td>
                    <td><s:property value="#film.director" /></td>
                    <td><s:property value="#film.type" /></td>
                    <td><s:property value="#film.language" /></td>
                    <td><s:property value="#film.score" /></td>
                    <td><s:property value="#film.description" /></td>
                    <td>
                        <button class="button" onclick="update('<s:property value='id'/>')">编辑</button>
                        <button class="button" onclick="confirmDelete('<s:property value='id'/>')">删除</button>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
</div>

</div>
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
    function confirmDelete(Id) {
        if(confirm("确认删除？")){
            window.location.href = "deleteFilmById.action?id=" + Id;
        }
    }
    function update(Id) {
        window.location.href = "getFilmById.action?id=" + Id;
    }
    function addFilm() {
        window.location.href = "addFilm.jsp";
    }
</script>
</body>
</html>