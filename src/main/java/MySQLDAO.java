import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MySQLAdsDao implements Blogposts {

    private Connection connection = null;

    public MySQLAdsDao(Config config){
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
//                    config.getConnection()
//                    config.getUser(),
//                    config.getPassword()
                    "jdbc:mysql://localhost/adlister_db?" +
                            "user=adlister&password=adlisterpassword"
            );
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    @Override
    public ArrayList<Ad> all() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ads");
            return generateAds(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Long insert(Ad ad) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createInsertQuery(ad), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating a new ad.", e);
        }
    }

    private String createInsertQuery(Ad ad) {
        return "INSERT INTO ads(user_id, title, description) VALUES "
                + "(" + ad.getUserId() + ","
                + "'" + ad.getTitle() + "', "
                + "'" + ad.getDescription() + "')";
    }

    private Ad extractAd(ResultSet rs) throws SQLException {
        return new Ad(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("title"),
                rs.getString("description")
        );
    }

    private ArrayList<Ad> generateAds(ResultSet rs) throws SQLException {
        ArrayList<Ad> ads = new ArrayList<>();
        while (rs.next()) {
            ads.add(new Ad(
                    rs.getLong("ads_id"),
                    rs.getLong("user_id"),
                    rs.getString("title"),
                    rs.getString("description")
            ));
        }
        System.out.println(ads);
        return ads;
    }

}
