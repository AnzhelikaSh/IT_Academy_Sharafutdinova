import java.sql.*;
import java.util.Scanner;

public class Playlist {

    public static class DB {
       static String url = "jdbc:postgresql://localhost:5432/postgres";
        static String username = "postgres";
        static String password = "postgres";
    }

    public static boolean existName(String name) {
        String sql = "select exists(select 1 from account where name = (?))";
        try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean existSong(String song) {
        String sql = "select exists(select 1 from music where song = (?))";
        try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, song);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(String name) {
        String sql = "INSERT INTO account (name) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void addSong(String name, String song, double song_length) {
        String sql = "with new_song as (\n" +
                "    insert into music (song, song_length)\n" +
                "        values (?, ?)\n" +
                "        returning id\n" +
                ")\n" +
                "insert into account_music (account_id, music_id)\n" +
                "select a.id, ns.id\n" +
                "from account a, new_song ns\n" +
                "where a.name = (?);";
        try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, song);
            ps.setDouble(2, song_length);
            ps.setString(3, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int act;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите номер действия: \n1. Добавить пользователя \n2. Добавить песню" +
                "\n3. Добавить песню в плейлист пользователя \n4. Добавить все песни автора в плейлист" +
                "\n5. Показать плейлист пользователя \n6. Показать все песни автора");
        while (true) {
            act = scanner.nextInt();
            if (act >= 1 && act <= 6) {
                break;
            }
            System.out.println("Ошибка! Число должно быть от 1 до 6.");
        }
        scanner.nextLine();
        if (act == 1) {
            System.out.println("Введите имя пользователя");
            String name = scanner.nextLine();
            boolean add = addUser(name);
            if (add) {
                System.out.println("Пользователь добавлен");
            } else {
                System.out.println("Пользователя не удалось добавить");
            }
        };

        if (act == 2) {
            System.out.println("Введите название песни");
            String song = scanner.nextLine();
            System.out.println("Введите автора");
            String name = scanner.nextLine();
            System.out.println("Введите длину песни");
            double song_length = Double.parseDouble(scanner.nextLine());
            if (existName(name)) {
                    addSong(name,song, song_length);
                } else {
                    boolean add = addUser(name);
                    addSong(name,song, song_length);
                }
                    };
        if (act == 3) {
            System.out.println("Введите название песни");
            String song = scanner.nextLine();
            System.out.println("Введите пользователя");
            String name = scanner.nextLine();
            String sql = "insert into playlist (account_id, music_id)\n" +
                    "values (\n" +
                    "        (select id from account where name = (?)),\n" +
                    "        (select id from music where song = (?)));";
                if (existName(name)) {
                        if (existSong(song)) {
                            try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
                                 PreparedStatement ps = connection.prepareStatement(sql)) {
                                ps.setString(1, name);
                                ps.setString(2, song);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                //System.out.println("Not connected");
                            }
                        } else {
                            System.out.println("Такой песни нет");
                        }
                } else {
                    System.out.println("Такого пользователя нет");
                }
          };


        if (act == 4) {
            System.out.println("Введите автора");
            String author = scanner.nextLine();
            System.out.println("Введите пользователя");
            String name = scanner.nextLine();
            String sql = "insert into playlist (account_id, music_id)\n" +
                    "select\n" +
                    "    u.id,\n" +
                    "    am.music_id\n" +
                    "from account_music am\n" +
                    "join account a on a.id = am.account_id\n" +
                    "join account u on u.name = (?)\n" +
                    "where a.name = (?);";
                if (existName(name)) {
                        if (existName(author)) {
                            try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
                                 PreparedStatement ps = connection.prepareStatement(sql)) {
                                ps.setString(1, name);
                                ps.setString(2, author);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Нет такого автора");
                        }
                } else {
                    System.out.println("Такого пользователя нет");
                }
        };

        if (act == 5) {
            System.out.println("Введите пользователя");
            String name = scanner.nextLine();
            String sql = "select\n" +
                    "    string_agg(a.name, ', ') as authors,\n" +
                    "    m.song,\n" +
                    "    m.song_length\n" +
                    "from playlist p\n" +
                    "         join account u on u.id = p.account_id\n" +
                    "         join music m on m.id = p.music_id\n" +
                    "         join account_music am on am.music_id = m.id\n" +
                    "         join account a on a.id = am.account_id\n" +
                    "where u.name = (?)\n" +
                    "group by a.name, m.song, m.song_length;";
            if (existName(name)) {
                try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
                     PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String authors = rs.getString("authors");
                        String song = rs.getString("song");
                        double length = rs.getDouble("song_length");
                        System.out.println(authors + " | " + song + " | " + length);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Такого пользователя нет");
            }
        };


        if (act == 6) {
            System.out.println("Введите автора");
            String name = scanner.nextLine();
            String sql = "select\n" +
                    "    m.song,\n" +
                    "    m.song_length\n" +
                    "from account_music am\n" +
                    "         join account a on a.id = am.account_id\n" +
                    "         join music m on m.id = am.music_id\n" +
                    "where a.name = (?)\n" +
                    "group by a.name, m.song, m.song_length;";
            if (existName(name)) {
                try (Connection connection = DriverManager.getConnection(DB.url, DB.username, DB.password);
                     PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String song = rs.getString("song");
                        double length = rs.getDouble("song_length");
                        System.out.println(song + " | " + length);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Такого автора нет");
            }
        };
    }
}
