<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>电影后台管理系统</title>
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
        .slideshow-container {
            max-width: 80vh;
            position: relative;
            margin: auto;
        }

        .mySlides {
            display: none;
        }

        .slideshow-container img {
            width: 100%;
            height: auto;
        }

        /* 添加轮播箭头样式 */
        .prev, .next {
            cursor: pointer;
            position: absolute;
            top: 50%;
            width: auto;
            margin-top: -22px;
            padding: 16px;
            color: white;
            font-weight: bold;
            font-size: 18px;
            transition: 0.6s ease;
            border-radius: 0 3px 3px 0;
            user-select: none;
        }
        /* 样式化标题 */
        h2 {
            font-family: 'Roboto', sans-serif; /* 设置字体 */
            font-size: 32px; /* 设置字号 */
            font-weight: 700; /* 设置字重 */
            color: deepskyblue; /* 设置颜色 */
            text-align: center; /* 设置文本居中 */
            text-transform: uppercase; /* 转换为大写 */
            margin-bottom: 10px; /* 设置下边距 */
        }

        .news {
            text-align: left;

        }

        .next {
            right: 0;
            border-radius: 3px 0 0 3px;
        }

        .prev:hover, .next:hover {
            background-color: rgba(0, 0, 0, 0.8);
        }
        /* 设置新闻项容器的样式 */
        .news-item {
            margin-bottom: 20px;
            border: 1px solid #ccc;
            padding: 10px;
        }

        /* 设置新闻标题的样式 */
        .news-item h3 {
            font-size: 18px;
            margin: 0;
            padding-bottom: 10px;
        }

        /* 设置超链接的样式 */
        .news-item a {
            color: #333;
            text-decoration: none;
        }

        /* 当鼠标悬停在超链接上时，改变文字颜色和下划线 */
        .news-item a:hover {
            color: #0088cc;
            text-decoration: underline;
        }

        /* 设置最后一个新闻项容器的样式 */
        .news-item:last-child {
            margin-bottom: 0;
            border-bottom: none;
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
    <%--在中心显示欢迎来到系统+用户名--%>
    <h2>亲爱的 <s:property value="%{#session.UserInfo.username}"/>,欢迎来到电影后台管理系统</h2>
    <div class="slideshow-container">
        <div class="mySlides">
            <img src="img/1.jpg" alt="Image 1">
        </div>
        <div class="mySlides">
            <img src="img/2.jpg" alt="Image 2">
        </div>
        <div class="mySlides">
            <img src="img/3.jpg" alt="Image 3">
        </div>
        <div class="mySlides">
            <img src="img/4.jpg" alt="Image 4">
        </div>
        <div class="mySlides">
            <img src="img/5.jpg" alt="Image 5">
        </div>
        <!-- 添加轮播箭头 -->
        <a class="prev" onclick="plusSlides(-1)">&#10094;</a>
        <a class="next" onclick="plusSlides(1)">&#10095;</a>
    </div>
    <%--提示用户新闻推荐--%>
    <div id="news-list">
        <h2 class="news">新闻推荐</h2>
        <s:if test="%{#session.newsRecommend != null}">
            <s:iterator value="%{#session.newsRecommend}">
                <div class="news-item">
                    <h3><a href="getNewsById.action?id=<s:property value='id' />"><s:property value='title' /></a></h3>
                </div>
            </s:iterator>
        </s:if>
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

    var slideIndex = 1;
    showSlides(slideIndex);

    function plusSlides(n) {
        showSlides(slideIndex += n);
    }

    function showSlides(n) {
        var i;
        var slides = document.getElementsByClassName("mySlides");
        if (n > slides.length) {
            slideIndex = 1;
        }
        if (n < 1) {
            slideIndex = slides.length;
        }
        for (i = 0; i < slides.length; i++) {
            slides[i].style.display = "none";
        }
        slides[slideIndex - 1].style.display = "block";
    }
</script>
</body>
</html>
