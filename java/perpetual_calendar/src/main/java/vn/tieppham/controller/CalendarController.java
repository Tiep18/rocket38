package vn.tieppham.controller;

import vn.tieppham.dao.UserDao;
import vn.tieppham.entity.User;
import vn.tieppham.utils.ScannerUtils;

import java.time.LocalDate;
public class CalendarController {
    private static final UserDao userDao = new UserDao();
    public static void register() {
        User user = new User();
        System.out.println("----------Register-------------");
        while (true) {
            String name = inputName();
            user.setName(name);
            String password = inputPassword();
            user.setPassword(password);
            if(userDao.register(user)) {
                return;
            }
        }
    }
    public static void login() {
        User user = new User();
        System.out.println("----------Log in-------------");
        while (true) {
            String name = inputName();
            user.setName(name);
            String password = inputPassword();
            user.setPassword(password);
            User loggedInUser = userDao.login(user);
            if(loggedInUser != null) {
                calendarUtilities(loggedInUser);
                return;
            }
        }
    }
    private static void calendarUtilities(User user) {
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
                    new UserDao().logout(user);
                    return;
                default:
                    System.out.println("The option is invalid, please try again!");
            }
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
