package as.leap.code.assist.classes;

import as.leap.code.assist.Path;
import as.leap.las.sdk.LASObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 钱包日志
 * User：poplar
 * Date：15-6-2
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("/walletLogs")
public class WalletLog extends LASObject {
  //用户ID
  private String uId;
  //钱包ID
  private String wId;
  //0增加，1减少
  private int opType;
  //0其它购买，1发票购买
  private int payType;
  //数量
  private Double amount;
  //货币种类
  private String coinId;

  public String getuId() {
    return uId;
  }

  public void setuId(String uId) {
    this.uId = uId;
  }

  public String getwId() {
    return wId;
  }

  public void setwId(String wId) {
    this.wId = wId;
  }

  public int getOpType() {
    return opType;
  }

  public void setOpType(int opType) {
    this.opType = opType;
  }

  public int getPayType() {
    return payType;
  }

  public void setPayType(int payType) {
    this.payType = payType;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getCoinId() {
    return coinId;
  }

  public void setCoinId(String coinId) {
    this.coinId = coinId;
  }
}