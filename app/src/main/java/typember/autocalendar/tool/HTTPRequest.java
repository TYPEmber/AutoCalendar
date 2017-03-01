package typember.autocalendar.tool;

/**
 * Created by nono0 on 2016/11/22.
 */
import android.app.Activity;

import com.google.api.client.util.NullValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import typember.autocalendar.activity.AutoCalendarActivity;

import static typember.autocalendar.activity.SettingActivity.forAPI;

/**
 * Created by lizhengxian on 2016/10/22.
 */

public class HTTPRequest {
    /**
     转半角的函数(DBC case)<br/><br/>
     全角空格为12288，半角空格为32
     其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param input 任意字符串
     * @return 半角字符串
     *
     */
    /*public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '!'|| c[i] == '！') {
                //感叹号处理
                c[i] = '`';
                continue;
            }
            if (c[i] == 12288) {
                //全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] == 12290) {
                //全角句号为12290，半角句号为32
                c[i] = (char) 46;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }*/
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                //全角空格为12288，半角空格为32
                c[i] = ' ';
                continue;
            }
            if (c[i] == 12290) {
                //全角句号为12290，半角句号为32
                c[i] = '.';
                continue;
            }

            if (c[i] > 65280 && c[i] < 65375)
                //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    private static final String SPLIT_CHAR = " ";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static String apiKey[] =
            {   "c8d7u9v3pNhHQqRaaxWC1X6h9ZNZ6zaSKthnzSOC",
                    "M1U4L7O9U9t7J935j0U6JOxvNcRAziHcVHcxQj7A"
            };
    private static String apiServer[] =
            {   "http://api.ltp-cloud.com/analysis/",
                    "http://ltpapi.voicecloud.cn/analysis/"
            };
    /**
     * 使用get方式请求分词服务
     *
     * @param text 原始中文字符串
     * @return 分词词组
     */
    public static void getSplitChar(final String text,final IResponse response) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                String result = null;
                HttpURLConnection conn = null;
                String textDBC=ToDBC(text);

                try {
                    textDBC = URLEncoder.encode(textDBC,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int select = forAPI.bufferAPIkey;

                StringBuilder urlBuilder = new StringBuilder(apiServer[select])//"http://api.ltp-cloud.com/analysis/"
                        .append('?').append("api_key").append('=').append(apiKey[select])//选择apiKey，避免冲突//"D8W3a5b4fvVngvMYrx0cfHOj5bKlrXlx6iiwsGNn"
                        .append('&').append("text").append('=').append(textDBC)
                        .append('&').append("pattern").append('=').append("ws")
                        .append('&').append("format").append('=').append("plain");
                /*StringBuilder urlBuilder = new StringBuilder("http://ltpapi.voicecloud.cn/analysis/")
                        .append('?').append("api_key").append('=').append("M1U4L7O9U9t7J935j0U6JOxvNcRAziHcVHcxQj7A")//利用讯飞分词API实现
                        .append('&').append("text").append('=').append(textDBC)
                        .append('&').append("pattern").append('=').append("ws")
                        .append('&').append("format").append('=').append("plain");*/
                String url = urlBuilder.toString();
                try {
                    // 利用string url构建URL对象
                    URL mURL = new URL(url);
                    conn = (HttpURLConnection) mURL.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(10000);

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        result = getStringFromInputStream(is);
                        if (response != null) {
                            response.finish(result == null ? null : result.split(SPLIT_CHAR));
                        }
                    } else {
                        if (response != null){
                            response.failure("访问失败" + responseCode + ':' + conn.getResponseMessage());
                        }
                    }
                } catch (Exception e) {
                    if (response != null){
                        response.failure(e.toString());
                    }
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        });
    }

    /**
     * 读取流,返回字符串
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        char[] c = state.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\n') {
                //感叹号处理
                c[i] = ' ';//这里正好利用了char转string会识别为""空值，以此实现换行

                continue;
            }
        }
        os.close();
        String bufferString = String.valueOf(c);
        return bufferString;
    }
}
