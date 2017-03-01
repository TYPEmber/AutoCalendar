package typember.autocalendar.tool;

/**
 * Created by nono0 on 2016/11/22.
 */

public interface IResponse {
    void finish(String[] words);
    void failure(String errorMsg);
}
