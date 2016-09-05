/**
 * Data Transfer Object for success response information
 */
package kz.ya.authcore.dto;

/**
 *
 * @author YERLAN
 */
public class SuccessData extends Data {
    
    private String apiToken;
    private String apiTokenExpirationDate;

    public SuccessData(String apiToken, String apiTokenExpirationDate) {
        this.apiToken = apiToken;
        this.apiTokenExpirationDate = apiTokenExpirationDate;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiTokenExpirationDate() {
        return apiTokenExpirationDate;
    }

    public void setApiTokenExpirationDate(String apiTokenExpirationDate) {
        this.apiTokenExpirationDate = apiTokenExpirationDate;
    }
}
