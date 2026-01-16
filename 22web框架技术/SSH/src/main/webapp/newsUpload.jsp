<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>新闻更新</title>
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
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2); /* 添加阴影效果 */
            height: 100vh; /* 侧边栏高度与视口高度一致 */
            position: fixed; /* 将侧边栏固定在屏幕上 */
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
        #news-form {
            margin: 20px auto;
            /*background: white; !* 白色背景 *!*/
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* 更细微的阴影 */
            width: 50%;
            box-sizing: border-box;
            border-radius: 8px; /* 圆角边框 */
        }
        #news-form input[type="text"],
        #news-form input[type="date"],
        #news-form textarea {
            width: calc(100% - 30px);
            padding: 15px;
            margin-bottom: 15px;
            border: 2px solid #e0e0e0; /* 更粗的边框 */
            border-radius: 8px; /* 圆角边框 */
            box-sizing: border-box;
            transition: border-color 0.3s; /* 边框颜色过渡效果 */
        }
        #news-form input[type="text"]:focus,
        #news-form input[type="date"]:focus,
        #news-form textarea:focus {
            border-color: #a0a0a0; /* 聚焦时边框颜色变化 */
        }
        #news-form textarea {
            height: 150px;
        }
        #news-form input[type="submit"] {
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
        #news-form input[type="submit"]:hover {
            background: #00CED1; /* 鼠标悬停时按钮颜色变化 */
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
    <div id="news-form">
        <h1>上传新闻</h1>
        <form action="addNews.action" method="post">
            <input type="text" id="title" name="title" placeholder="标题"><br><br>
            <input type="text" id="author" name="author" placeholder="作者"><br><br>
            <input type="date" id="date" name="date" placeholder="日期"><br><br>
            <textarea id="description" name="description" placeholder="描述"></textarea><br><br>
            <input type="submit" value="提交新闻">
        </form>
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

