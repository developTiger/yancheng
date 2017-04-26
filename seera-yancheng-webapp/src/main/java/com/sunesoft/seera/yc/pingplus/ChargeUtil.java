package com.sunesoft.seera.yc.pingplus;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.sunesoft.seera.fr.msg.email.EmailMessage;
import com.sunesoft.seera.fr.utils.Configs;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bing on 16/8/22.
 */
public class ChargeUtil {

    private static String appId;
    private static String apiKey;
    private static String publicKeyPath;
    private static Logger logger = LoggerFactory.getLogger(ChargeUtil.class);

    private static String email_host;
    private static String email_sender;
    private static String email_password;

    static {
        appId = Configs.getProperty("seera.yancheng.pingpp.appid");
        apiKey = Configs.getProperty("seera.yancheng.pingpp.appkey");
        publicKeyPath = SeeraCharge.class.getClassLoader().getResource("rsa_public_key.pem").getPath();
        Pingpp.apiKey = apiKey;
        Pingpp.privateKeyPath = SeeraCharge.class.getClassLoader().getResource("rsa_private_key.pem").getPath();

        email_host=Configs.getProperty("email_host");
        email_sender=Configs.getProperty("email_sender");
        email_password=Configs.getProperty("email_password");
    }

    public static EmailMessage emailMessage(){
        EmailMessage emailMessage=new EmailMessage();
        emailMessage.setHost(email_host);
        emailMessage.setSender(email_sender);
        emailMessage.setPassword(email_password);
        return emailMessage;
    }

    public static String createQr(BigDecimal amount,String orderName, String orderNum, String clientIp, String channel) {
        Charge charge = createCharge(amount, orderName,orderNum, clientIp, channel);
        if (charge != null && charge.getCredential() != null && !charge.getCredential().isEmpty()) {
            if (charge.getCredential().containsKey(channel)) {
                return (String) charge.getCredential().get(channel);
            }
        }
        return null;
    }

    /**
     * 创建 Charge
     * <p>
     * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
     * map 里面参数的具体说明请参考：https://pingxx.com/document/api#api-c-new
     *
     * @return Charge
     */
    public static Charge createCharge(BigDecimal amount,String orderName, String orderNum, String clientIp, String channel) {
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<>();
        chargeMap.put("amount", amount.multiply(BigDecimal.valueOf(100)));//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        chargeMap.put("currency", "cny");
        chargeMap.put("subject",  orderName);
        chargeMap.put("body", "淹城官方商城购票支付");
        chargeMap.put("order_no", orderNum);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("channel", channel);// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("client_ip", clientIp); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        Map<String, String> app = new HashMap<>();
        app.put("id", appId);
        chargeMap.put("app", app);

        Map<String, Object> extra = new HashMap<>();
        switch (channel) {
            case "alipay_wap":
            case "alipay_pc_direct":
                extra.put("success_url", "http://store.cn-yc.com.cn/alipay_order_confirm");
                break;
            case "upacp_pc":
                extra.put("result_url","http://store.cn-yc.com.cn/upacp_order_confirm");
                break;
            case "wx_pub_qr":
                extra.put("product_id", orderNum);
                break;
        }
        chargeMap.put("extra", extra);
        try {
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return charge;
    }

    /**
     * 创建 Charge (微信公众号)
     * <p>
     * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
     * map 里面参数的具体说明请参考：https://pingxx.com/document/api#api-c-new
     *
     * @return Charge
     */
    public static Charge createChargeWithOpenid(String openid, BigDecimal amount, String orderNum, String client_ip) {
        Charge charge = null;
//        Map<String, Object> chargeMap = new HashMap<String, Object>();
//        chargeMap.put("amount", amount);//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
//        chargeMap.put("currency", "cny");
//        chargeMap.put("subject", "Your Subject");
//        chargeMap.put("body", "Your Body");
//        //String orderNo = new Date().getTime() + Main.randomString(7);
//        String orderNo = "订单编号";
//        chargeMap.put("order_no", orderNum);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
//        chargeMap.put("channel", "wx_pub");// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
//        chargeMap.put("client_ip", client_ip); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
//        Map<String, String> app = new HashMap<String, String>();
//        app.put("id", appId);
//        chargeMap.put("app", app);
//
//        Map<String, Object> extra = new HashMap<String, Object>();
//        extra.put("open_id", openid);// 用户在商户微信公众号下的唯一标识，获取方式可参考 WxPubOAuthExample.java
//        chargeMap.put("extra", extra);
//        try {
//            //发起交易请求
//            charge = Charge.create(chargeMap);
//            // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
//            String chargeString = charge.toString();
//            System.out.println(chargeString);
//        } catch (PingppException e) {
//            e.printStackTrace();
//        }
        return charge;
    }


    /**
     * 查询 Charge
     * <p>
     * 该接口根据 charge Id 查询对应的 charge 。
     * 参考文档：https://pingxx.com/document/api#api-c-inquiry
     * <p>
     * 该接口可以传递一个 expand ， 返回的 charge 中的 app 会变成 app 对象。
     * 参考文档： https://pingxx.com/document/api#api-expanding
     *
     * @param id
     */
    public static Charge retrieve(String id) {
        Charge charge = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            charge = Charge.retrieve(id, params);
            System.out.println(charge);
        } catch (PingppException e) {
            e.printStackTrace();
        }

        return charge;
    }

    /**
     * 分页查询 Charge
     * <p>
     * 该接口为批量查询接口，默认一次查询10条。
     * 用户可以通过添加 limit 参数自行设置查询数目，最多一次不能超过 100 条。
     * <p>
     * 该接口同样可以使用 expand 参数。
     *
     * @return chargeCollection
     */
    public static ChargeCollection all() {
        ChargeCollection chargeCollection = null;
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);
        Map<String, String> app = new HashMap<>();
        app.put("id", appId);
        params.put("app", app);

        try {
            chargeCollection = Charge.all(params);
            System.out.println(chargeCollection);
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException | ChannelException e) {
            e.printStackTrace();
        }

        return chargeCollection;
    }

    /**
     * 验证签名
     *
     * @param dataString
     * @param signatureString
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifyData(String dataString, String signatureString)
            throws Exception {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(getPubKey());
        signature.update(dataString.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }

    /**
     * 获得公钥
     *
     * @return
     * @throws Exception
     */
    public static PublicKey getPubKey() throws Exception {
        //String pubKeyString = getStringFromFile(publicKeyPath);
        String  pubKeyString ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxDxKeuN50mCPUL6T3nqz5AR3mBhNPd+0Q/OcTzMJFVEUDdt7uVh1AIhR2H6eVG8aJYnDXrGxDe47r5MuM2Co1UcBZuYVkDnTU2apDGF4dceBxFNzvKgw0r+dgdwTqXHeKdOFb3g8Uo3T7Z2kk5Egotof8TaxxSiAV8kuwkUOo+lpcxCBaHtZqTpjmYSwkhKoKXeV1TJcXVNBRop2dUGEM5VizioXgw6uh2ajxxby8Yd5wGNJ6SUjkFbYwPHBvqZdpyV/cprD/fvGYQIU4IaBhYcjK6Hhlw+LuglQrJxFUwQb/VZVihkSuuS75JCQhCP4DFm1/p3UMXvaXHlgJ+8inQIDAQAB";
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }

    /**
     * 读取文件, 部署 web 程序的时候, 签名和验签内容需要从 request 中获得
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getStringFromFile(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
        BufferedReader bf = new BufferedReader(inReader);
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = bf.readLine();
            if (line != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } while (line != null);
        return sb.toString();
    }
    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }
}
