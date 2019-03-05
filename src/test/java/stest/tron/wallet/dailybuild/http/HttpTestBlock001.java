package stest.tron.wallet.dailybuild.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import stest.tron.wallet.common.client.Configuration;
import stest.tron.wallet.common.client.utils.HttpMethed;

@Slf4j
public class HttpTestBlock001 {

  private JSONObject responseContent;
  private HttpResponse response;
  private String httpnode = Configuration.getByPath("testng.conf").getStringList("httpnode.ip.list")
      .get(0);
  private Integer currentBlockNum;
  private JSONObject blockContent;
  private String blockId;

  /**
   * constructor.
   */
  @Test(enabled = true, description = "Get now block by http")
  public void get1NowBlock() {
    response = HttpMethed.getNowBlock(httpnode);
    logger.info("code is " + response.getStatusLine().getStatusCode());
    Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
    responseContent = HttpMethed.parseResponseContent(response);
    blockContent = responseContent;
    blockId = responseContent.get("blockID").toString();
    HttpMethed.printJsonContent(responseContent);
    Assert.assertTrue(responseContent.size() >= 2);
    responseContent = HttpMethed.parseStringContent(responseContent.get("block_header").toString());
    Assert.assertTrue(responseContent.size() >= 2);
    Assert.assertFalse(responseContent.get("witness_signature").toString().isEmpty());
    HttpMethed.printJsonContent(responseContent);
    responseContent = HttpMethed.parseStringContent(responseContent.get("raw_data").toString());
    HttpMethed.printJsonContent(responseContent);
    Assert.assertTrue(Integer.parseInt(responseContent.get("number").toString()) > 0);
    currentBlockNum = Integer.parseInt(responseContent.get("number").toString());
    Assert.assertTrue(Long.parseLong(responseContent.get("timestamp").toString()) > 1550724114000L);
    Assert.assertFalse(responseContent.get("witness_address").toString().isEmpty());
  }

  /**
   * constructor.
   */
  @Test(enabled = true, description = "Get block by num by http")
  public void get2BlockByNum() {
    response = HttpMethed.getBlockByNum(httpnode, currentBlockNum);
    Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
    responseContent = HttpMethed.parseResponseContent(response);
    Assert.assertEquals(responseContent, blockContent);

  }

  /**
   * constructor.
   */
  @Test(enabled = true, description = "GetBlockByLimitNext by http")
  public void get3BlockByLimitNext() {
    response = HttpMethed.getBlockByLimitNext(httpnode, currentBlockNum - 10, currentBlockNum);
    Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
    responseContent = HttpMethed.parseResponseContent(response);
    HttpMethed.printJsonContent(responseContent);
    logger.info(responseContent.get("block").toString());
    JSONArray jsonArray = JSONArray.parseArray(responseContent.get("block").toString());
    Assert.assertEquals(jsonArray.size(), 10);
  }

  /**
   * constructor.
   */
  @Test(enabled = true, description = "GetBlockByLastNum by http")
  public void get4BlockByLastNum() {
    response = HttpMethed.getBlockByLastNum(httpnode, 8);
    Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
    responseContent = HttpMethed.parseResponseContent(response);
    HttpMethed.printJsonContent(responseContent);
    logger.info(responseContent.get("block").toString());
    JSONArray jsonArray = JSONArray.parseArray(responseContent.get("block").toString());
    Assert.assertEquals(jsonArray.size(), 8);
  }

  /**
   * constructor.
   */
  @Test(enabled = true, description = "GetBlockById by http")
  public void get5BlockById() {
    response = HttpMethed.getBlockById(httpnode, blockId);
    Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
    responseContent = HttpMethed.parseResponseContent(response);
    HttpMethed.printJsonContent(responseContent);
    Assert.assertEquals(blockId, responseContent.get("blockID").toString());
  }

  /**
   * constructor.
   */
  @Test(enabled = true, description = "List nodes by http")
  public void get6ListNodes() {
    response = HttpMethed.listNodes(httpnode);
    responseContent = HttpMethed.parseResponseContent(response);
    HttpMethed.printJsonContent(responseContent);
  }


  /**
   * constructor.
   */
  @Test(enabled = true, description = "get next maintenance time by http")
  public void get7NextMaintaenanceTime() {
    response = HttpMethed.getNextmaintenanceTime(httpnode);
    responseContent = HttpMethed.parseResponseContent(response);
    HttpMethed.printJsonContent(responseContent);
    Assert.assertFalse(responseContent.get("num").toString().isEmpty());
  }


  /**
   * constructor.
   */
  @Test(enabled = true, description = "get chain parameter by http")
  public void get8ChainParameter() {
    response = HttpMethed.getChainParameter(httpnode);
    responseContent = HttpMethed.parseResponseContent(response);
    HttpMethed.printJsonContent(responseContent);
    JSONArray jsonArray = JSONArray.parseArray(responseContent.get("chainParameter").toString());
    Assert.assertTrue(jsonArray.size() >= 26);

  }

  /**
   * constructor.
   */
  @Test(enabled = true, description = "get Node Info by http")
  public void get9NodeInfo() {
    response = HttpMethed.getNodeInfo(httpnode);
    responseContent = HttpMethed.parseResponseContent(response);
    HttpMethed.printJsonContent(responseContent);
    Assert.assertFalse(responseContent.get("configNodeInfo").toString().isEmpty());

  }

  /**
   * constructor.
   */
  @AfterClass
  public void shutdown() throws InterruptedException {
    HttpMethed.disConnect();
  }
}
