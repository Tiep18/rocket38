package vn.tieppham.controller;

import vn.tieppham.dao.UserDao;
import vn.tieppham.entity.User;
import vn.tieppham.utils.ScannerUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
public class CalendarController {
    private static boolean isLogin = false;
    public static void register() {
        try {
            User user = new User();
            System.out.println("----------Register-------------");
            while (true) {
                String name = inputName();
                user.setName(name);
                    if(UserDao.getByName(user) == null) {
                        break;
                    }
                    System.err.println("Name already existed");
            }
            String password = inputPassword();
            user.setPassword(password);
            UserDao.insert(user);
            System.out.println("Register successfully");
        } catch (SQLException | IOException e) {
            System.err.println("Register failure");
            System.out.println(e);
        }
    }
    public static void login() {
        try {
            User user = new User();
            System.out.println("----------Log in-------------");
            while (true) {
                String name = inputName();
                user.setName(name);
                String password = inputPassword();
                user.setPassword(password);
                User loggedInUser = UserDao.getByNameAndPassword(user);
                if(loggedInUser != null) {
                    isLogin = true;
                    System.out.println("Login successfully");
                    calendarUtilities();
                    return;
                }
                System.err.println("Name or password is not correct");
            }
        } catch (SQLException | IOException e) {
            System.err.println("Login failure");
            System.out.println(e);
        }
    }
    public static void logout() {
        isLogin = false;
        System.out.println("Log out successfully");
    }
    private static void calendarUtilities() {
        if(isLogin) {
            while (true) {
                System.out.println("--------Calendar Utilities---------");
                System.out.println("1. Get current date");
                System.out.println("2. Change solar date to lunar date");
                System.out.println("3. Log out");
                int opt = ScannerUtils.inputInt();
                switch (opt) {
                    case 1:
                        LocalDate date = LocalDate.now();
                        System.out.println("Today is " + date);
                        break;
                    case 2:
                        System.out.println("This feature is in development");
                        break;
                    case 3:
                        logout();
                        return;
                    default:
                        System.err.println("The option is invalid, please try again!");

                }
          }
        } else {
            System.out.println("You are not logged in");
        }

    }
    private static String inputName() {
        System.out.println("Please enter your username:");
        return ScannerUtils.inputName();
    }

    private static String inputPassword() {
        System.out.println("Please enter your password:");
        return ScannerUtils.inputPassword();
    }
}
