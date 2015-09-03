/**
 * 
 */
package net.sweetcaptcha.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author VladislavKondratenko
 *
 */
public class SweetCaptcha {

    private enum Method {
        GET_HTML("get_html"), CHECK("check");
        private String value;

        private Method(String value) {
            this.value = value;
        }
    }

    private static final String LANGUAGE_PROPERTY = "language";

    private static final String AUTOSUBMIT_PROPERTY = "is_auto_submit";

    private static final String PLATFORM_PROPERTY = "platform";

    private static final String METHOD_PROPERTY = "method";

    private static final String APP_ID_PROPERTY = "app_id";

    private static final String APP_KEY_PROPERTY = "app_key";

    private static final String SC_KEY_PROPERTY = "sckey";

    private static final String SC_VALUE_PROPERTY = "scvalue";

    private static final String DEFAULT_API_HOST = "http://sweetcaptcha.com/api";

    private static final String DEFAULT_PLATFORM = "sweetcaptcha-java";

    private String appId;

    private String appKey;

    private String language;

    public SweetCaptcha(String appId, String appKey) {
        if (appId == null || appKey == null) {
            throw new IllegalArgumentException("App id and app key shall not be empty");
        }
        this.appId = appId;
        this.appKey = appKey;
    }

    public SweetCaptcha(String appId, String appKey, String language) {
        this(appId, appKey);
        this.language = language;
    }

    public String getHtml() throws IOException {
        List<NameValuePair> urlParameters = getDefaultParams();
        urlParameters.add(new BasicNameValuePair(METHOD_PROPERTY, Method.GET_HTML.value));
        if (language != null) {
            urlParameters.add(new BasicNameValuePair(LANGUAGE_PROPERTY, language));
        }
        urlParameters.add(new BasicNameValuePair(AUTOSUBMIT_PROPERTY, "1"));

        return sendRequest(urlParameters);
    }

    public Boolean check(String scKey, String scValue) throws IOException {
        List<NameValuePair> urlParameters = getDefaultParams();

        urlParameters.add(new BasicNameValuePair(SC_KEY_PROPERTY, scKey));
        urlParameters.add(new BasicNameValuePair(SC_VALUE_PROPERTY, scValue));
        urlParameters.add(new BasicNameValuePair(METHOD_PROPERTY, Method.CHECK.value));

        return Boolean.valueOf(sendRequest(urlParameters));
    }

    private List<NameValuePair> getDefaultParams() {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair(APP_ID_PROPERTY, appId));
        urlParameters.add(new BasicNameValuePair(APP_KEY_PROPERTY, appKey));
        urlParameters.add(new BasicNameValuePair(PLATFORM_PROPERTY, DEFAULT_PLATFORM));
        return urlParameters;
    }

    private String sendRequest(List<NameValuePair> urlParameters) throws IOException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(DEFAULT_API_HOST);
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(request);
            return IOUtils.toString(response.getEntity().getContent());
        }

    }

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @return the appKey
     */
    public String getAppKey() {
        return appKey;
    }

}
