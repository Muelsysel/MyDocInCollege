<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2024/5/29
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2024/5/29
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户信息修改</title>
    <style>
        /* 添加CSS样式来创建更美观的侧边栏布局 */
        body {
            font-family: 'Arial', sans-serif; /* 设置默认字体 */
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
        }
        #sidebar {
            position: fixed; /* 固定侧边栏位置 */
            top: 0; /* 顶部对齐 */
            left: 0; /* 左侧对齐 */
            width: 250px;
            /*background: #87CEFA;*/
            position: fixed; /* 将侧边栏固定在屏幕上 */
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            height: 100vh;
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
            margin-left: 270px; /* 根据侧边栏宽度调整 */
            padding: 20px;
            /*background: #F8F8F8;*/
            min-height: 100vh;
        }











        body {
            font-family: 'Arial', sans-serif;
            text-align: center;
            /*background-color: #f4f4f4;*/
        }

        .container {
            margin: 20px auto;
            /*background: white;*/
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            padding: 40px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 30%;
            box-sizing: border-box;
            border-radius: 8px;
            transition: transform 0.3s ease-in-out;
        }

        .container:hover {
            transform: translateY(-10px);
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
            /*background-color: #f4f4f4;*/
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
    <!-- 功能内容将在这里显示 -->
    <div class="container">
        <h1>修改个人信息</h1>
        <form action="saveUserWithoutPassword" method="post">
            <input type="hidden" name="id" value="<s:property value="#session.UserId" />"/>
            <div class="input-group">
                <label for="username">用户名：</label>
                <input type="text" id="username" name="username" value="<s:property value="#session.UserInfo.username" />">
            </div>
            <div class="input-group">
                <label for="phone">电话：</label>
                <input type="text" id="phone" name="phone" value="<s:property value="#session.UserInfo.phone" />">
            </div>
            <div class="input-group">
                <label for="email">邮箱：</label>
                <input type="text" id="email" name="email" value="<s:property value="#session.UserInfo.email" />">
            </div>
            <input type="submit" value="保存">
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




