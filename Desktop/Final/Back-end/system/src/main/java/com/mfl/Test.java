package com.mfl;

import com.mfl.config.Jdbc;

import java.sql.Connection;

public class Test {
    public static void main(String[] args) throws Exception {
        Jdbc.list();
        Connection con= Jdbc.getConnection();
        System.out.println(con);

    }
}
