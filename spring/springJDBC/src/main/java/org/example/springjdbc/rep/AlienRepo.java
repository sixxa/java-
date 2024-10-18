package org.example.springjdbc.rep;

import org.example.springjdbc.model.Alien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlienRepo {
    private JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }
    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }
    public void save(Alien alien) {
        String sql = "insert into alien (id, name, tech) values (?, ?, ?)";
        int row = template.update(sql, alien.getId(), alien.getName(), alien.getTech());
        System.out.println("affected row: " + row);
    }
    public List<Alien> findAll() {
        String sql = "select * from alien";
      /*  RowMapper<Alien> mapper = new RowMapper<Alien>() {
            @Override
            public Alien mapRow(ResultSet rs, int rowNum) throws SQLException {
                Alien a = new Alien();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setTech(rs.getString("tech"));
                return a;
            }
        }; */
                List<Alien> aliens = template.query(sql, (rs,row) -> {
                Alien a = new Alien();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setTech(rs.getString("tech"));
                return a;

        });
      //  List<Alien> aliens = template.query(sql, mapper);
        return aliens;
    }
}
