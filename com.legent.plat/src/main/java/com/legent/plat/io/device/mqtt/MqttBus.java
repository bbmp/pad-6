package com.legent.plat.io.device.mqtt;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.legent.LogTags;
import com.legent.VoidCallback;
import com.legent.io.buses.AbsNioBus;
import com.legent.io.msgs.IMsg;
import com.legent.io.msgs.collections.BytesMsg;
import com.legent.plat.Plat;
import com.legent.utils.LogUtils;
import com.legent.utils.api.MediaUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.StorageUtils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;

public class MqttBus extends AbsNioBus implements MqttCallback {

    public final static String TAG = LogTags.TAG_IO;
    protected final static int QOS = 0;
    protected final static boolean CLEAN_START = true;

    protected MqttParams busParams;
    protected MqttConnectOptions conOpt;
    protected MqttDefaultFilePersistence dataStore;
    protected MqttAsyncClient client;
    protected Set<String> topics = Sets.newHashSet();
    protected Set<String> topicsBackup = Sets.newHashSet();

    /**
     * params @MqttParams
     */
    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        Preconditions.checkArgument(params.length >= 1);
        busParams = (MqttParams) params[0];
        Preconditions.checkNotNull(busParams, "MqttBus parmas is null");

        File path = new File(busParams.dataStorePath);
        if (!path.exists()) {
            path.mkdir();
        }

        dataStore = new MqttDefaultFilePersistence(busParams.dataStorePath);

        conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(CLEAN_START);
        conOpt.setUserName(busParams.user);
        conOpt.setPassword(busParams.password.toCharArray());
        conOpt.setKeepAliveInterval(busParams.keepAliveInterval);
        conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        if (busParams.isSSL) {
            conOpt.setSSLProperties(getSSLSettings());
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        try {
            if (dataStore != null) {
                dataStore.clear();
                dataStore.close();
                dataStore = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onOpen(final VoidCallback callback) {
        Log.e("mqtt", "mqttbus start connect");
        try {

            if (!NetworkUtils.isConnect(cx)) {
                onCallFailure(callback, new Throwable("mqtt open faild:invalid network "));
                return;
            }

            if (client != null) {
                client.disconnectForcibly();//内部有保护措施 会调用close()
//                client.close();
                client = null;
            }

            String brokerUrl = String.format("%s%s:%s",
                    busParams.isSSL ? "ssl://" : "tcp://", busParams.host,
                    busParams.port);
            client = new MqttAsyncClient(brokerUrl, busParams.clientId,
                    dataStore);
            client.setCallback(this);
            client.connect(conOpt, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken token) {

                    onCallSuccess(callback);
                    Log.e("mqtt", "mqttbus start connect suc");
                }

                @Override
                public void onFailure(IMqttToken token, Throwable t) {
                    Log.e("mqtt", "mqttbus start connect fail");
                    onCallFailure(callback, t);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            onCallFailure(callback, e.getCause());
        }
    }

    @Override
    protected void onClose(final VoidCallback callback) {
        Log.e("mqtt", "mqttbus start close ");
        try {
            client.disconnect(null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken token) {
                    onCallSuccess(callback);
                    Log.e("mqtt", "mqttbus start close suc");
                }

                @Override
                public void onFailure(IMqttToken token, Throwable t) {
                    onCallFailure(callback, t);
                    Log.e("mqtt", "mqttbus start close fail");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            onCallFailure(callback, e.getCause());
        }
    }

    @Override
    protected void onConnectionChanged(boolean isConnected) {

        if (isConnected) {
            if (topicsBackup != null && topicsBackup.size() > 0) {
                final List<String> list = Lists.newArrayList(topicsBackup);

                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        subscribe(list);
                    }
                });
            }
        }

        super.onConnectionChanged(isConnected);
    }

    @Override
    public void send(IMsg msg, VoidCallback callback) {

        LogUtils.e("20190425", "msg:" + msg.getID());
        publish(msg, callback);
    }

    // -------------------------------------------------------------------------------
    // MqttCallback start
    // -------------------------------------------------------------------------------

    @Override
    public void connectionLost(Throwable t) {

        Log.e(TAG, "mqtt 断开");
        t.printStackTrace();

        topicsBackup.clear();
        topicsBackup.addAll(topics);
        topics.clear();

        onConnectionChanged(false);
        startReconnect();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage msg) throws Exception {
        try {
            if (Plat.DEBUG)
                LogUtils.e("20170324", "mqttbusmsg:" + Arrays.toString(msg.getPayload()));
            BytesMsg bm = new BytesMsg(msg.getPayload());
            bm.setTag(topic);
            MqttBus.this.onMsgReceived(bm);
        } catch (Exception e) {
            Log.e(TAG, "mqtt error on messageArrived");
            Log.e(TAG, String.format("topic: %s \tMqttMessage:%S", topic, msg));
        }
    }

    // -------------------------------------------------------------------------------
    // MqttCallback end
    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------
    // topic
    // -------------------------------------------------------------------------------

    /**
     * 订阅一组主题
     */
    synchronized public void subscribe(final List<String> topicList) {
        if (topicList == null || topicList.size() == 0) {
            Log.w(TAG, "topicList is null");
            return;
        }

        if (client == null) {
            Log.w(TAG, "mqtt client is null");
            return;
        }
        if (!client.isConnected()) {
            Log.w(TAG, "mqtt is disconnected");
            return;
        }

        final List<String> list = Lists.newArrayList();
        for (String topic : topicList) {
            if (topics.contains(topic)) {
                Log.w(TAG, "过滤重复订阅:" + topic);
            } else {
                LogUtils.i("Subscribe", "topic:" + topic);
                list.add(topic);
            }
        }

        if (list.size() == 0) {
            return;
        }

        String[] topicFilters = new String[list.size()];
        list.toArray(topicFilters);

        int[] qos = new int[list.size()];
        Arrays.fill(qos, QOS);

        try {
            Log.d(TAG, "MQTT 开始订阅");
            client.subscribe(topicFilters, qos, null,
                    new IMqttActionListener() {

                        @Override
                        public void onSuccess(IMqttToken token) {
                            Log.d(TAG, "MQTT 订阅成功:" + list);
                            topics.addAll(list);
                        }

                        @Override
                        public void onFailure(IMqttToken token, Throwable t) {
                            Log.w(TAG, "MQTT 订阅失败:" + t.getMessage());
                        }
                    });
        } catch (MqttException e) {
            Log.e(TAG, "MQTT 订阅异常:" + e.getMessage());
        }

    }

    /**
     * 取消订阅一组主题
     *
     * @param topicList
     */
    public void unsubscribe(final List<String> topicList) {

        if (topicList == null || topicList.size() == 0) {
            Log.w(TAG, "topicList is null");
            return;
        }

        if (client == null) {
            Log.w(TAG, "mqtt client is null");
            return;
        }
        if (!isConnected) {
            Log.w(TAG, "mqtt is disconnected");
            return;
        }

        final List<String> list = Lists.newArrayList();
        for (String topic : topicList) {
            if (!topics.contains(topic)) {
                Log.w(TAG, "过滤不存在的订阅:" + topic);
            } else {
                list.add(topic);
            }
        }

        if (list.size() == 0) {
            return;
        }

        String[] topicFilters = new String[list.size()];
        list.toArray(topicFilters);

        try {
            Log.d(TAG, "MQTT 开始取消订阅");
            client.unsubscribe(topicFilters, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken token) {
                    Log.d(TAG, "MQTT 取消订阅成功" + list);
                    topics.removeAll(list);
                }

                @Override
                public void onFailure(IMqttToken token, Throwable t) {
                    Log.w(TAG, "MQTT 取消订阅失败:" + t.getMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "MQTT 取消订阅异常:" + e.getMessage());
        }
    }

    /**
     * 订阅一个主题
     *
     * @param topic
     */
    synchronized public void subscribe(final String topic) {
        if (Strings.isNullOrEmpty(topic)) {
            Log.w(TAG, "topic is null");
            return;
        }

        if (client == null) {
            Log.w(TAG, "mqtt client is null");
            return;
        }
        if (!isConnected) {
            Log.w(TAG, "mqtt is disconnected");
            return;
        }

        if (topics.contains(topic)) {
            Log.w(TAG, "重复订阅:" + topic);
            return;
        }

        try {
            Log.d(TAG, "MQTT 开始订阅");
            client.subscribe(topic, QOS, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken token) {
                    Log.d(TAG, "MQTT 订阅成功:" + topic);
                    topics.add(topic);
                }

                @Override
                public void onFailure(IMqttToken token, Throwable t) {
                    Log.w(TAG, "MQTT 订阅失败:" + t.getMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "MQTT 订阅异常:" + e.getMessage());
        }

    }

    /**
     * 取消订阅一个主题
     *
     * @param topic
     */
    synchronized public void unsubscribe(final String topic) {
        if (client == null) {
            return;
        }

        if (!client.isConnected()) {
            Log.w(TAG, "mqtt is disconnected");
            return;
        }

        if (Strings.isNullOrEmpty(topic)) {
            Log.w(TAG, "topic is null");
            return;
        }

        if (!topics.contains(topic)) {
            Log.w(TAG, "取消订阅不存在:" + topic);
            return;
        }

        try {
            Log.d(TAG, "MQTT 开始取消订阅");
            client.unsubscribe(topic, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken token) {
                    Log.d(TAG, "MQTT 取消订阅成功" + topic);
                    topics.remove(topic);
                }

                @Override
                public void onFailure(IMqttToken token, Throwable t) {
                    Log.w(TAG, "MQTT 取消订阅失败:" + t.getMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "MQTT 取消订阅异常:" + e.getMessage());
        }

    }

    // -------------------------------------------------------------------------------
    // protecetd
    // -------------------------------------------------------------------------------

    /**
     * 发布消息到主题
     */
    protected void publish(final IMsg msg, final VoidCallback callback) {

        if (client == null || !client.isConnected()) {
            onCallFailure(callback, new Exception("mqtt is disconnected"));
            return;
        }


        try {
            String topic = msg.getTag();
            if (Plat.DEBUG)
                LogUtils.i("StopKaiJi", "topic::" + topic);
            byte[] data = msg.getBytes();

            Preconditions.checkNotNull(topic, "invalid topic");

            MqttMessage message = new MqttMessage(data);
            message.setQos(QOS);

            client.publish(topic, message, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken token) {
                    LogUtils.i("20171111", " client.publish::onSuccess");
                    onCallSuccess(callback);
                }

                @Override
                public void onFailure(IMqttToken token, Throwable t) {
                    onCallFailure(callback, t);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------------
    // about SLL
    // -------------------------------------------------------------------------------

    public Properties getSSLSettings() {
        final Properties properties = new Properties();
        properties.setProperty("com.ibm.ssl.keyStore",
                "C:/BKSKeystore/mqttclientkeystore.keystore");
        properties.setProperty("com.ibm.ssl.keyStoreType", "BKS");
        properties.setProperty("com.ibm.ssl.keyStorePassword", "passphrase");
        properties.setProperty("com.ibm.ssl.trustStore",
                "C:/BKSKeystore/mqttclienttrust.keystore");
        properties.setProperty("com.ibm.ssl.trustStoreType", "BKS");
        properties.setProperty("com.ibm.ssl.trustStorePassword", "passphrase ");

        return properties;
    }

    // -------------------------------------------------------------------------------
    // MqttParams
    // -------------------------------------------------------------------------------

    /**
     * mqtt 连接参数
     *
     * @author sylar
     */
    public static class MqttParams {
        public String user;
        public String password;
        public String clientId;
        public String host;
        public int port;
        public int keepAliveInterval;
        public boolean isSSL = false;
        public String dataStorePath;

        public MqttParams() {
            this.host = Plat.serverOpt.acsHost;
            this.port = Plat.serverOpt.acsPort;
            this.user = "rokiDevice";
            this.password = "roki2014";
            this.keepAliveInterval = 30;
            this.isSSL = false;
            this.dataStorePath = String.format("%s/%s/",
                    StorageUtils.getCachPath(Plat.app), "mqtt");
        }

        public MqttParams(String clientId) {
            this();
            this.clientId = clientId;
        }

        public MqttParams(String clientId, boolean isSSL) {
            this(clientId);
            this.isSSL = isSSL;
        }

        public MqttParams(String clientId, String user, String password) {
            this(clientId);
            this.user = user;
            this.password = password;
        }
    }

    /**
     * static SSLSocketFactory getSocketFactory (final String caCrtFile, final
     * String crtFile, final String keyFile, final String password) throws
     * Exception { Security.addProvider(new BouncyCastleProvider());
     *
     * // load CA certificate PEMReader reader = new PEMReader(new
     * InputStreamReader(new
     * ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
     * X509Certificate caCert = (X509Certificate)reader.readObject();
     * reader.close();
     *
     * // load client certificate reader = new PEMReader(new
     * InputStreamReader(new
     * ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))));
     * X509Certificate cert = (X509Certificate)reader.readObject();
     * reader.close();
     *
     * // load client private key reader = new PEMReader( new
     * InputStreamReader(new
     * ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFile)))), new
     * PasswordFinder() { public char[] getPassword() { return
     * password.toCharArray(); } } ); KeyPair key =
     * (KeyPair)reader.readObject(); reader.close();
     *
     * // CA certificate is used to authenticate server KeyStore caKs =
     * KeyStore.getInstance("JKS"); caKs.load(null, null);
     * caKs.setCertificateEntry("ca-certificate", caCert); TrustManagerFactory
     * tmf = TrustManagerFactory.getInstance("PKIX"); tmf.init(caKs);
     *
     * // client key and certificates are sent to server so it can authenticate
     * us KeyStore ks = KeyStore.getInstance("JKS"); ks.load(null, null);
     * ks.setCertificateEntry("certificate", cert);
     * ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
     * new java.security.cert.Certificate[]{cert}); KeyManagerFactory kmf =
     * KeyManagerFactory.getInstance("PKIX"); kmf.init(ks,
     * password.toCharArray());
     *
     * // finally, create SSL socket factory SSLContext context =
     * SSLContext.getInstance("TLSv1"); context.init(kmf.getKeyManagers(),
     * tmf.getTrustManagers(), null);
     *
     * return context.getSocketFactory(); }
     */

}
