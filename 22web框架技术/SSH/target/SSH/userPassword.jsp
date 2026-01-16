<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>密码修改</title>
    <style>
        /* 添加CSS样式来创建更美观的侧边栏布局 */
        body {
            font-family: 'Arial', sans-serif; /* 设置默认字体 */
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
        }
        #sidebar {
            float: left;
            width: 250px;
            /*background: #87CEFA; /* 侧边栏背景颜色 */
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
            /*background: #F8F8F8; /* 内容区域背景颜色 */
            min-height: 100vh; /* 内容区域最小高度与视口高度一致 */
        }





        body {
            font-family: 'Arial', sans-serif;
            text-align: center;
            /*background-color: #f4f4f4;*/
        }
        .form-container {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin-top: -100px; /* 更多向上移动 */
        }

        #password-update {
            background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
            padding: 80px; /* 增加内边距 */
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 500px; /* 增加宽度 */
            height: 450px; /* 增加高度 */
            border-radius: 8px;
        }

        .input-group {
            margin-bottom: 20px;
        }
        .input-group label {
            display: block;
            margin-bottom: 5px;
        }
        .input-group input {
            width: 100%;
            padding: 20px; /* 增加内边距使输入框更粗 */
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        .input-group input:focus {
            border-color: #a0a0a0;
        }
        #password-update button {
            width: 100%;
            padding: 15px 0;
            background: #87CEFA;
            color: white;
            cursor: pointer;
            transition: background-color 0.3s;
            border-radius: 20px;
            border: none;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            margin-top: 20px;
        }
        #password-update button:hover {
            background: #00CED1;
        }
        #password-update h1 {
            color: #333;
            margin-bottom: 50px;
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
    <div class="form-container">
        <div id="password-update">
            <h1>修改个人密码</h1> <!-- 添加的标题 -->
            <form action="updatePassword.action" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="id" value="<s:property value="#session.UserId" />"/>
                <div class="input-group">
                    <label for="password1">新密码：</label>
                    <input type="password" id="password1" placeholder="请输入新密码">
                </div>
                <div class="input-group">
                    <label for="password2">确认新密码：</label>
                    <input type="password" id="password2" name="password" placeholder="请再次输入新密码">
                </div>
                <button type="submit">提交</button>
            </form>
        </div>
    </div>

</div>
<script>
function validateForm() {
    var newPassword1 = document.getElementById('password1').value;
    var newPassword2 = document.getElementById('password2').value;

    if (newPassword1 !== newPassword2) {
        alert("两次输入的新密码不一致，请重新输入！");
        return false;
    }
    return true;
}
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