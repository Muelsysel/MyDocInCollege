package Mapper;

import POJO.UserInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class UserMapper {

    // 根据账号密码进行查询
    public boolean compare(String account, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("user.txt")).getPath()))) {
            String line;
            Pattern pattern = Pattern.compile("\\s+");

            while ((line = reader.readLine()) != null) {
                String[] info = pattern.split(line);
                //String[] info = line.split("\\s\\s+"); // 根据空格分隔用户信息
                if (info[0].equals(account) && info[1].equals(password)) {

                    reader.close();
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 注册账号,将账号密码手机号按照升序排列插入use.txt中
    public void register(String account, String password, String phone) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader userFileReader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("user.txt")).getPath()))) {
            String line;
            //跳过首行
            userFileReader.readLine();
            while ((line = userFileReader.readLine()) != null) {
                lines.add(line);
            }

            // 将新账号密码和手机号插入到原有的txt文件中，按照账号升序排序
            int i;
            for (i = 0; i < lines.size(); i++) {
                String[] fields = lines.get(i).split("\t");
                if (fields[0].compareTo(account) > 0) {
                    break;
                }
            }
            lines.add(i, account + "\t" + password + "\t" + phone);

            // 写入文件
            writeToFile(lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //写入文件
    private void writeToFile(List<String> lines) {
        try (BufferedWriter clearFileWriter = new BufferedWriter(new FileWriter(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("user.txt")).getPath()))) {
            clearFileWriter.write("用户账号   密码  联系方式");
            clearFileWriter.newLine();
            for (String line : lines) {
                clearFileWriter.write(line);
                clearFileWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //根据账号查询用户信息
    public UserInfo getUser(String account) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("user.txt")).getPath()))) {
            String line;
            Pattern pattern = Pattern.compile("\\s+");
            UserInfo userInfo = new UserInfo();
            while ((line = reader.readLine()) != null) {
                String[] info = pattern.split(line);
                if (info[0].equals(account)) {
                    userInfo.setAccount(info[0]);
                    userInfo.setPassword(info[1]);
                    userInfo.setPhone(info[2]);

                    reader.close();
                    return userInfo;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //更新用户信息
    public void updateUserInfoFile(UserInfo userInfo) throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader userFileReader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("user.txt")).getPath()))) {
            String line;
            Pattern pattern = Pattern.compile("\\s+");
            userFileReader.readLine();//跳过首行
            while ((line = userFileReader.readLine()) != null) {
                String[] fields = pattern.split(line);
                if (!Objects.equals(fields[0], userInfo.getAccount())) {
                    lines.add(line);
                } else {
                    lines.add(userInfo.getAccount() + "\t" + userInfo.getPassword() + "\t" + userInfo.getPhone());
                }
            }
            // 写入文件
            writeToFile(lines);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //删除用户信息
    public void deleteUserInfoFile(UserInfo userInfo) throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader userFileReader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("user.txt")).getPath()))) {
            String line;
            Pattern pattern = Pattern.compile("\\s+");
            userFileReader.readLine();//跳过首行
            while ((line = userFileReader.readLine()) != null) {
                String[] fields = pattern.split(line);
                if (!Objects.equals(fields[0], userInfo.getAccount())) {//排除需要删除的用户
                    lines.add(line);
                }
            }
            // 写入文件
            writeToFile(lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
