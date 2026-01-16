<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2024/5/28
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>电影展示</title>
    <style>
        /* 添加CSS样式来创建更美观的侧边栏布局 */
        body {
            font-family: 'Arial', sans-serif; /* 设置默认字体 */
            overflow-x: hidden;
            display: flex;
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
        }
        /*#sidebar {*/
        /*    float: left;*/
        /*    width: 250px;*/
        /*    !*    background: #87CEFA; !* 侧边栏背景颜色 *!*!*/
        /*    padding: 20px;*/
        /*    box-shadow: 0 2px 5px rgba(0,0,0,0.2); !* 添加阴影效果 *!*/
        /*    height: 100vh; !* 侧边栏高度与视口高度一致 *!*/
        /*}*/
        #sidebar {
            position: fixed; /* 将侧边栏固定在屏幕上 */
            top: 0;
            left: 0;
            width: 250px;
            /* background: #87CEFA; !* 侧边栏背景颜色 *! */
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
            background: #efcde6;
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
            /*//background: #F8F8F8; !* 内容区域背景颜色 *!*/
            min-height: 100vh; /* 内容区域最小高度与视口高度一致 */
        }


        .movie-card {
            margin-bottom: 20px;
            text-align: center;
            width: 19%; /* 一行五个，每个占 19% 的宽度，留下一些间隔 */
            position: relative; /* 设置相对定位，用于定位评分元素 */
        }

        .movie-gallery {
            display: flex; /* 使用 flex 布局 */
            flex-wrap: wrap; /* 允许换行 */
            justify-content: space-between; /* 平均分布子元素 */
        }

        .movie-card {
            margin-bottom: 20px;
            text-align: center;
            width: 19%; /* 每个占 19% 的宽度 */
            position: relative; /* 设置相对定位，用于定位评分元素 */
        }

        .movie-card img {
            width: 100%;
            height: auto; /* 让图片高度自适应 */
            object-fit: cover; /* 等比例缩放并填充 */
        }

        .movie-info {
            padding: 10px;
        }

        .movie-score {
            position: absolute;
            top: 0px; /* 距离顶部的距离 */
            right: 2px; /* 距离右边的距离 */
            background-color: #ff6e07; /* 黄色背景 */
            color: #fff; /* 白色字体 */
            padding: 0px 10px; /* 内边距 */
            font-size: 14px; /* 字体大小 */
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
    <!-- 一排五个电影图片 -->
    <div class="movie-gallery">
        <s:iterator value="%{#session.Films}" var="film" status="status">
            <div class="movie-card" onclick="window.location.href='getFilmById?id=<s:property value='#film.id' />'">
                <img src="<s:property value="#film.url" />" alt="<s:property value="#film.name" />" style="height:300px;width: 245px">
                <div class="movie-score">
                    <p><s:property value="#film.score" />分</p>
                </div>
                <div class="movie-info">
                    <h2><s:property value="#film.name" /></h2>
                </div>
            </div>
        </s:iterator>
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

</script>
</body>
</html>