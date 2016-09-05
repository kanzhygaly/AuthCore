/**
 * Data Transfer Object for request information
 */
package kz.ya.authcore.dto;

/**
 *
 * @author YERLAN
 */
public class InData extends Data {
    
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
