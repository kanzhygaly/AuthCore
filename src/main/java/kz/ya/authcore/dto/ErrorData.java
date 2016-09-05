/**
 * Data Transfer Object for error response information
 */
package kz.ya.authcore.dto;

/**
 *
 * @author YERLAN
 */
public class ErrorData extends Data {
    
    private String errorDescription;
    private String errorCode;

    public ErrorData(String errorDescription, String errorCode) {
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
