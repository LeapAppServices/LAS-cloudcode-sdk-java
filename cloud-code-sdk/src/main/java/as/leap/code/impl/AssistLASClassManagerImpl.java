package as.leap.code.impl;

import as.leap.code.*;
import as.leap.code.assist.AssistLASClassManager;
import as.leap.code.assist.Path;
import as.leap.las.sdk.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.*;

/**
 * User：poplar
 * Date：15-6-2
 */
public class AssistLASClassManagerImpl<T> implements AssistLASClassManager<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssistLASClassManagerImpl.class);
  private Class<T> tClass;
  String apiAddress;

  public AssistLASClassManagerImpl(Class<T> tClass) {
    this.tClass = tClass;
    String path = this.tClass.getAnnotation(Path.class).value();
    this.apiAddress = CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX + path;
  }

  @Override
  public FindMsg<T> find(LASQuery query) {
    try {
      String response = WebUtils.doPost(apiAddress + "/query", CloudCodeContants.HEADERS, serializeLasQueryForPostQuest(query), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of find[" + apiAddress + "/query]:" + response);
      JsonNode jsonNode = LASJsonParser.asJsonNode(response);
      ArrayNode results = (ArrayNode) jsonNode.get("results");
      int c = 0;
      List<T> r = new ArrayList<T>();
      if (results != null) {
        c = results.size();
        for (JsonNode result : results) {
          r.add(LASJsonParser.asObject(result.toString(), tClass));
        }
      }
      return new FindMsg<T>(c, r);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public SaveMsg create(T obj) throws as.leap.code.LASException {
    try {
      String response = WebUtils.doPost(apiAddress, CloudCodeContants.HEADERS, LASJsonParser.asJson(obj), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of create[" + apiAddress + "]:" + response);
      return LASJsonParser.asObject(response, SaveMsg.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public T findById(String id) {
    try {
      String response = WebUtils.doGet(apiAddress + "/" + id, CloudCodeContants.HEADERS, null);
      LOGGER.info("get response of findById[" + apiAddress + "/" + id + "]:" + response);
      if ("{}".equals(response)) return null;
      return LASJsonParser.asObject(response, tClass);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public UpdateMsg update(String id, LASUpdate update) throws as.leap.code.LASException {
    try {
      String response = WebUtils.doPut(apiAddress + "/" + id, CloudCodeContants.HEADERS, LASJsonParser.asJson(update.update()), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of update[" + apiAddress + "/" + id + "](" + update.update() + "):" + response);
      return LASJsonParser.asObject(response, UpdateMsg.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public DeleteMsg delete(String id) {
    try {
      String response = WebUtils.doDelete(apiAddress + "/" + id, CloudCodeContants.HEADERS, null);
      LOGGER.info("get response of delete[" + apiAddress + "/" + id + "]:" + response);
      return LASJsonParser.asObject(response, DeleteMsg.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  String serializeLasQueryForPostQuest(LASQuery lasQuery) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (lasQuery.query() != null) map.put("where", LASJsonParser.asJson(lasQuery.query()));
    if (lasQuery.sort() != null) map.put("order", lasQuery.sort());
    if (lasQuery.keys() != null) map.put("keys", lasQuery.keys());
    if (lasQuery.includes() != null) map.put("include", lasQuery.includes());
    map.put("limit", lasQuery.limit());
    map.put("skip", lasQuery.skip());
//    map.put("excludeKeys", null); Unsupported.
    return LASJsonParser.asJson(map);
  }

}
