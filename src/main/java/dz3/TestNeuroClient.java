package dz3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

public class TestNeuroClient {

    /* хеш для подписи */
    public static final String digest = "1F4A121121123123189002BC";
    /* 16-ричное представление публичного ключа сервиса */
    public static String publicKey16 = "30819f300d06092a864886f70d010101050003818d0030818902818100d58c14f2b46c7993f35d7f1139f764f0a043b8c67b8ade55292b029b4517733ee3fa991f6ebd8eefba334539ab45bda3d7d9890f50a4a34b6ee13d10c78bbd4d7202d23ba2e6696ef13828a407551c56b533b38e839a297f1548766386c09b82a058daf10aa2f2d1f34f480b1b0736bd1c41ec0c759a0f3270a8d065834e31130203010001";
    public static String privateKey16 = "30820277020100300d06092a864886f70d0101010500048202613082025d02010002818100d58c14f2b46c7993f35d7f1139f764f0a043b8c67b8ade55292b029b4517733ee3fa991f6ebd8eefba334539ab45bda3d7d9890f50a4a34b6ee13d10c78bbd4d7202d23ba2e6696ef13828a407551c56b533b38e839a297f1548766386c09b82a058daf10aa2f2d1f34f480b1b0736bd1c41ec0c759a0f3270a8d065834e31130203010001028180483abc6f2755b57db488404d7c54d0808f44b8bff1d393c62c498ef523d67db59cf90b47d5762a5dad965fe8d4a49f3346f41e66deb9013ea77f69df3e0f66f4a7ae25dbb9f00536c6327a28cf745ec5e92ba5914667d6994ac645fecfc4a4ed8a3109b5650975748aba2ba3ca08397b5ce5ce4e505e063ae65d5881d39581f1024100f5159c56a79e7596264137275c6a01f211d1b43ac92b0b2052e2f8171b2c09fb039227f1fb1c894f6362567022d95c5ba156ab96cb3916569d13d82ad43e08e9024100df0ee450df3292fa8e754539288df8c5884e640564cc950b06e88a0206b8656373b9b54434a4edff25c009c6f949a21ecb2848c8ec83a851a6a7966ed048ec9b024100bb1156b691d4927e1c0ef5313709b71874df72c8d819e1b1377304d86626142c238aa0b0c3f20120b6842de403c5930861ba8cd8599fe65c1023408158654d59024100a55d5af281a5f2c5021bfa87d782f92d28524560a24bf12acda253be10d15f890d605dfc04b34925e10cafb2ed59237184d786ae3e7994ad83c9b682ccb3638502401726a7aaa8ee393eabcd80e54d03a18964ecf2c97ee63135417c7baf8c4536fc9c603d3cdf672b1c512114831f069c89d75a2896cf63394140ded6771bd37032";

    /* алгоритм ключа сервиса */
    public static final String KEY_ALGORITHM = "RSA";
    /* алгоритм подписи, формируемой сервисом */
    public static final String SIGN_ALGORITHM = "SHA256withRSA";

    public static String weights = "";

    public static void main(String[] args) {
        getChain("1331a5b51ad36063ac9a1bae21c5598ae404397cc1c2c491a3ddd0157891db8d");
        sendBlock();
    }

    public static void sendBlock() {

        SignService service = new SignService();

        String prevHash = null;
        if (BlockChain.chain.size() > 0) {
            try {
                prevHash = new String(Hex.encode(service.getHash(BlockChain.chain.get(BlockChain.chain.size() - 1))));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String dataStr = "{\"w11\":\"0.1\",\"w12\":\"0.1\",\"w21\":\"0.1\",\"w22\":\"0.1\",\"v11\":\"0.1\",\"v12\":\"0.1\",\"v13\":\"0.1\",\"v21\":\"0.1\",\"v22\":\"0.1\",\"v23\":\"0.1\",\"w1\":\"0.1\",\"w2\":\"0.1\",\"w3\":\"0.1\",\"e\":\"0.6778807714293326\",\"publickey\":\"" + publicKey16 + "\"}";
        try {

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Hex.decode(privateKey16.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);

            byte[] sign = service.generateRSAPSSSignature(priKey, "Гилязов Азат 11-902".getBytes());
            String signature = new String(Hex.encode(sign));
            System.out.println(signature);

            String block = "{\"prevhash\":\"" + prevHash + "\",\"data\":" + dataStr + ",\"signature\":\"" + signature + "\"}";
            System.out.println(block);

            URL url = new URL("http://89.108.115.118/nbc/newblock?block=" + URLEncoder.encode(block, "UTF-8"));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            int rcode = con.getResponseCode();

            if (rcode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String response = reader.readLine();

                ObjectMapper mapper = new ObjectMapper();
                NewBlockResponse resp = mapper.readValue(response, NewBlockResponse.class);
                if (resp.getStatus() == 0) {
                    BlockModel newBlock = resp.getBlock();
                    System.out.println(newBlock);
                } else {
                    System.out.println(resp.getStatusString());
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    // Запрос блокчейна
    public static void getChain(String hash) {
        try {
            //URL url = new URL("http://188.93.211.195/newblock?block=");
            URL url = new URL("http://89.108.115.118/nbc/chain" + (hash != null ? "?hash=" + hash : ""));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            int rcode = con.getResponseCode();
            System.out.println(con.getResponseCode());

            if (rcode == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<BlockModel> blockChain = mapper.readValue(con.getInputStream(), new TypeReference<List<BlockModel>>() {
                });

                if (blockChain != null) {
                    BlockChain.chain = blockChain;
                    BlockChain.chain.forEach(blockModel -> {
                        System.out.println(blockModel.toString());
                    });
                }

                System.out.println(publicKey16);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean verify(String publicKeyHexStr, byte[] data, String signHexStr) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHM, "BC");

            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Hex.decode(publicKeyHexStr));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            signature.initVerify(pubKey);

            signature.update(data);

            return signature.verify(Hex.decode(signHexStr));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}