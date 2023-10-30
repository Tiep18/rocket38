package vn.tieppham;

import vn.tieppham.controller.CalendarController;
import vn.tieppham.dao.UserDao;
import vn.tieppham.entity.User;
import vn.tieppham.utils.MysqlConnection;
import vn.tieppham.utils.ScannerUtils;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        final int REGISTER = 1;
        final int LOGIN = 2;
        final int EXIT = 3;
        while (true) {
            System.out.println("--------Perpetual Calendar-----------");
            System.out.println(REGISTER + ". Register");
            System.out.println(LOGIN + ". Login");
            System.out.println(EXIT + ". Exit");
            int opt = ScannerUtils.inputInt();
            switch (opt) {
                case REGISTER:
                    CalendarController.register();
                case LOGIN:
                    CalendarController.login();
                    break;
                case EXIT:
                    System.out.println("Program exited");
                    return;
                default:
                    System.err.println("The option is invalid, please try again!");

            }
        }
    }
}
