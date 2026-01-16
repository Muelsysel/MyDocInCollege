@echo off
echo ========================================
echo   College Docs Git Auto Sync
echo ========================================

:: 1. 强制解决中文乱码显示
git config --global core.quotepath false

:: 2. 添加所有变动
echo Adding files...
git add .

:: 3. 获取日期和时间作为提交信息
set commit_date=%date:~0,4%-%date:~5,2%-%date:~8,2%
set commit_time=%time:~0,2%:%time:~3,2%
set commit_msg=Sync Docs: %commit_date% %commit_time%

:: 4. 提交
echo Committing changes...
git commit -m "%commit_msg%"

:: 5. 推送到 GitHub
echo.
echo Pushing to GitHub...
git push origin main

if %errorlevel% equ 0 (
    echo.
    echo [SUCCESS] Push Completed!
) else (
    echo.
    echo [ERROR] Something went wrong. Check your network or credentials.
)

echo ========================================
pause