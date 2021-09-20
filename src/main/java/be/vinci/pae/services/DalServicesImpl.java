package be.vinci.pae.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import be.vinci.pae.exceptions.DataException;
import be.vinci.pae.utils.Config;

public class DalServicesImpl implements DalBackendServices, DalServices {


  private BasicDataSource bds;
  private ThreadLocal<Connection> threadLoc;
  private Properties properties;


  /**
   * Constructor for class.
   */
  public DalServicesImpl() {
    threadLoc = new ThreadLocal<>();
    bds = new BasicDataSource();
    properties = new Properties();
    try {
      bds = BasicDataSourceFactory.createDataSource(initConnection());
    } catch (Exception e) {
      System.out.println("Constructeur DalServiceImpl");
      throw new DataException("DalServiceImpl constructor");
    }
  }


  private Properties initConnection() {
    properties.setProperty("driverClassName", "org.postgresql.Driver");
    properties.setProperty("url", Config.getProperty("DataBaseURL"));
    properties.setProperty("username", Config.getProperty("DataBaseUsername"));
    properties.setProperty("password", Config.getProperty("DataBasePassword"));
    return properties;
  }

  @Override
  public void startTransaction() {
    try {
      // tester s'il ya pas déja une transaction en cours min 33'
      Connection conn = bds.getConnection();
      conn.setAutoCommit(false);
      threadLoc.set(conn);
    } catch (SQLException e) {
      throw new DataException("startTransaction");
    }
  }

  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    PreparedStatement ps = null;
    Connection conn = threadLoc.get();
    try {
      ps = conn.prepareStatement(sql);
    } catch (SQLException e) {
      // Lancer un fatal exception VIDEO REVIEW 31'
      System.out.println("Error while preparing statements");
      System.exit(1);
    }
    return ps;
  }


  @Override
  public void commitTransaction() {
    Connection conn = threadLoc.get();
    try {
      conn.commit();
    } catch (SQLException e) {
      // min 36 exception
      throw new DataException("commitTransaction");
    }
    threadLoc.remove();
    try {
      conn.close();
    } catch (SQLException e) {
      throw new DataException("commitTransaction");
    }
  }

  @Override
  public void rollbackTransaction() {
    Connection conn = threadLoc.get();
    try {
      conn.rollback();
    } catch (SQLException e) {
      throw new DataException("rollBackTransaction");
    }
    threadLoc.remove();
    try {
      conn.close();
    } catch (SQLException e) {
      throw new DataException("rollBackTransaction");
    }
  }
}
