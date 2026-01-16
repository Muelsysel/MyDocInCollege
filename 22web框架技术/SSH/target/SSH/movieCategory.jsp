<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>电影类别</title>
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
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            margin: 0;
            padding: 0;
        }

        .form-container {
            display: flex;
            justify-content: flex-end; /* 向右对齐 */
            margin-bottom: 10px; /* 与表格间隔 */
        }

        /* Update the container style */
        #type-list {
            margin: 20px;
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }

        /* Update table styles */
        #type-list table {
            width: 100%;
            border-collapse: collapse;
            text-align: left; /* 确保所有列默认左对齐 */
        }

        #type-list th, #type-list td {
            padding: 8px;
            border-bottom: 1px solid #ddd;
        }

        #type-list .header {
            background: #87CEFA;
            color: white;
        }

        #type-list th {
            background: #87CEFA; /* 表头背景颜色 */
            color: white; /* 表头字体颜色 */
        }

        #type-list td {
            padding: 10px; /* 单元格内边距 */
        }

        #type-list tr:hover {
            background-color: #B0E0E6; /* 鼠标悬停时行的背景颜色 */
        }

        /* 将类型列居中显示 */
        #type-list th:nth-child(2), #type-list td:nth-child(2) {
            text-align: center;
        }


        /* 将操作列靠右显示 */


        #type-list th:last-child {
            text-align: center;
        }

        /* 保持操作列的单元格内容靠右显示 */
        #type-list td:last-child {
            display: flex;
            justify-content: center; /* 水平居中 */
            gap: 10px; /* 按钮之间的间隔 */
        }

        /* Update button styles */
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

        /* 保持按钮悬停效果 */
        .button:hover {
            background-color: #f361a5;
        }

        #newType {
            width: 200px; /* 输入框宽度 */
            padding: 10px 15px; /* 内边距，左右稍大 */
            font-size: 14px; /* 字体大小 */
            border: 1px solid #87CEFA; /* 边框颜色 */
            border-radius: 20px; /* 圆角边框 */
            outline: none; /* 去除焦点轮廓 */
            transition: all 0.3s; /* 平滑过渡效果 */
            margin-right: 10px; /* 与按钮间隔 */
        }

        #newType:focus {
            box-shadow: 0 0 8px rgba(135, 206, 250, 0.8); /* 聚焦时的阴影效果 */
            border-color: #B0E0E6; /* 聚焦时的边框颜色 */
        }

        #type-list h1 {
            margin-bottom: 10px; /* 减少标题与输入框之间的空间 */
        }

        /* 为输入框和按钮设置负的顶部外边距 */
        #newType, .button {
            margin-top: -10px; /* 将输入框和按钮上移 */
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
    <div id="type-list">
        <h1>电影类型列表</h1>

        <div class="form-container">
            <input type="text" id="newType" placeholder="新增类型">
            <button class="button" onclick="addType()">新增</button>
        </div>
        <table>
            <tr class="header">
                <th>ID</th>
                <th>类型</th>
                <th>操作</th>
            </tr>
            <s:iterator value="%{#session.Types}">
                <tr>
                    <td><s:property value="id" /></td>
                    <td><s:property value="type" /></td>
                    <td>
                        <button class="button" onclick="updateType('<s:property value='id'/>')">修改</button>
                        <button class="button" onclick="confirmDelete('<s:property value='id'/>')">删除</button>
                    </td>
                </tr>
            </s:iterator>
        </table>
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
        if (confirm("确定要删除这条类型吗？")) {
            window.location.href = "deleteType.action?id=" + Id;
        }
    }

    function updateType(Id) {
        //弹出输入框，输入类型，确认后调用updateNews.action
        var newType = prompt("请输入类型：", "");
        if (newType != null && newType!=="") {
            window.location.href = "updateType.action?id=" + Id + "&type=" + newType;
        }else if(newType===""){
            alert("类型不能为空");
        }
    }
    function addType() {
        var newType = document.getElementById('newType').value;
        if (newType != null && newType!==""&& newType!==undefined) {
            window.location.href = "addType.action?type=" + newType;
        }else{
            alert("类型不能为空");
        }
    }
</script>
</body>
</html>