/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bean.User;

/**
 *
 * @author ADMIN
 */
public interface UserDao {

    public boolean checkExistedUser(String username);

    public boolean insertUser(User user);

    public User getUserInfo(int userId);

    public int getUserId(String username);

    public boolean checkUserLogin(String username, String password);

    public boolean ChangePass(String username, String oldpassword, String newpassword);
}
