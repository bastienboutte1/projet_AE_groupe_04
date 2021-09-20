package be.vinci.pae.api.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.ws.rs.WebApplicationException;
import views.Views;

public class Json {
  private static final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * load data from file.
   * 
   * @param dbFilePath file for DB
   * 
   * @param collectionName name of the collection
   */
  public static <T> List<T> loadDataFromFile(String dbFilePath, String collectionName,
      Class<T> targetClass) {
    try {
      JsonNode node = jsonMapper.readTree(Paths.get(dbFilePath).toFile());
      JsonNode collection = node.get(collectionName);
      if (collection == null) {
        return new ArrayList<T>();
      }
      return jsonMapper.readerForListOf(targetClass).readValue(node.get(collectionName));

    } catch (FileNotFoundException e) {
      return new ArrayList<T>();
    } catch (IOException e) {
      return new ArrayList<T>();
    }
  }

  /**
   * save data to file.
   * 
   * @param list list
   * 
   * @param dbFilePath file for DB
   * 
   * @param collectionName name of the collection
   */
  public static <T> void saveDataToFile(List<T> list, String dbFilePath, String collectionName) {
    try {

      // get all collections
      Path pathToDb = Paths.get(dbFilePath);
      if (!Files.exists(pathToDb)) {
        // write a new collection to the db file
        ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(collectionName, list);
        jsonMapper.writeValue(pathToDb.toFile(), newCollection);
        return;
      }

      // get all collections
      JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

      // remove current collection
      if (allCollections.has(collectionName)) {
        ((ObjectNode) allCollections).remove(collectionName);
      }

      // create a new JsonNode and add it to allCollections
      String currentCollectionAsString = jsonMapper.writeValueAsString(list);
      JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
      ((ObjectNode) allCollections).putPOJO(collectionName, updatedCollection);

      // write to the db file
      jsonMapper.writeValue(pathToDb.toFile(), allCollections);
    } catch (IOException e) {
      throw new WebApplicationException("Error in json.java", e);
    }
  }

  /**
   * Load data from file based on view.
   * 
   * @param dbFilePath file for DB
   * 
   * @param jsonViewClass view class json
   */
  public static <T> List<T> loadDataFromFileBasedOnView(String dbFilePath, Class<?> jsonViewClass,
      String collectionName, Class<T> targetClass) {
    try {
      JsonNode node = jsonMapper.readTree(Paths.get(dbFilePath).toFile());
      // Get the type at execution because new TypeReference<List<T>>() is not allowed
      JavaType type = jsonMapper.getTypeFactory().constructCollectionType(List.class, targetClass);
      // deserialize using JSON Views : Internal View
      return jsonMapper.readerWithView(jsonViewClass).forType(type)
          .readValue(node.get(collectionName));

    } catch (FileNotFoundException e) {
      return new ArrayList<T>();
    } catch (IOException e) {
      return new ArrayList<T>();
    }
  }

  /**
   * Save data to file based on view.
   * 
   * @param list list
   * 
   * @param jsonViewClass class from json
   */
  public static <T> void saveDataToFileBasedOnView(List<T> list, Class<?> jsonViewClass,
      String dbFilePath, String collectionName) {
    try {
      // get all collections
      Path pathToDb = Paths.get(dbFilePath);
      if (!Files.exists(pathToDb)) {
        // write a new collection to the db file
        ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(collectionName, list);
        jsonMapper.writeValue(pathToDb.toFile(), newCollection);
        return;
      }

      JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

      // remove current collection
      if (allCollections.has(collectionName)) {
        ((ObjectNode) allCollections).remove(collectionName);
      }

      // create a new JsonNode and add it to allCollections
      String currentCollectionAsString =
          jsonMapper.writerWithView(jsonViewClass).writeValueAsString(list);
      // String currentCollectionAsString = jsonMapper.writeValueAsString(list);
      JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
      ((ObjectNode) allCollections).putPOJO(collectionName, updatedCollection);

      // write to the db file
      jsonMapper.writeValue(pathToDb.toFile(), allCollections);

    } catch (IOException e) {
      throw new WebApplicationException("Error in json.java saveDataToFileBasedOnView", e);
    }
  }

  /**
   * Serialize Public Json View.
   * 
   * @param item item
   */
  public static <T> String serializePublicJsonView(T item) {
    // serialize using JSON Views : Public View
    try {
      return jsonMapper.writerWithView(Views.Public.class).writeValueAsString(item);
    } catch (JsonProcessingException e) {
      return null;
    }

  }

  /**
   * Filter Public Json View as List.
   * 
   * @param list list
   * 
   * @param targetClass class target
   */
  public static <T> List<T> filterPublicJsonViewAsList(List<T> list, Class<T> targetClass) {

    try {
      System.out.println("List prior to serialization : " + list);
      JavaType type = jsonMapper.getTypeFactory().constructCollectionType(List.class, targetClass);
      // serialize using JSON Views : public view (all fields not required in the
      // views are set to null)
      String publicItemListAsString =
          jsonMapper.writerWithView(Views.Public.class).writeValueAsString(list);
      System.out.println("serializing public Json view: " + publicItemListAsString);
      // deserialize using JSON Views : Public View
      return jsonMapper.readerWithView(Views.Public.class).forType(type)
          .readValue(publicItemListAsString);

    } catch (JsonProcessingException e) {
      return null;
    }
  }

  /**
   * filter public Json View.
   * 
   * @param item item
   * 
   * @param targetClass targetted class
   */
  public static <T> T filterPublicJsonView(T item, Class<T> targetClass) {

    try {
      // serialize using JSON Views : public view (all fields not required in the
      // views are set to null)
      String publicItemAsString =
          jsonMapper.writerWithView(Views.Public.class).writeValueAsString(item);
      // deserialize using JSON Views : Public View
      return jsonMapper.readerWithView(Views.Public.class).forType(targetClass)
          .readValue(publicItemAsString);

    } catch (JsonProcessingException e) {
      return null;
    }
  }

}
