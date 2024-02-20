package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        User user1 = new User("Tijan", "Diop", (byte) 26);
        User user2 = new User("Dasha", "Palchkova", (byte) 27);
        User user3 = new User("Valera", "Buben", (byte) 28);
        User user4 = new User("Arthur", "Ponomov", (byte) 30);

        List <User> userList = List.of(user1, user2, user3, user4);
        userList.forEach(user -> userService.saveUser(user.getName(), user.getLastName(), user.getAge()));

        List <User> allUsers = userService.getAllUsers();
        allUsers.forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();

        try {
//            Util.closeConnect();
            Util.closeSessionFactory();
            System.out.println("Соединение с БД закрыто.");
        } catch (Exception e) {
            System.out.println("Ошибка закрытия соединения с БД");
            System.out.println(e.getMessage());
        }


    }
}
