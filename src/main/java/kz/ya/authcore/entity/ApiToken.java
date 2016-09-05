/**
 * Entity class for api token information
 */
package kz.ya.authcore.entity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author YERLAN
 */
@Entity
@Table(name = "api_token")
@NamedQueries({
    @NamedQuery(name = "ApiToken.findByValue", query = "SELECT t FROM ApiToken t WHERE t.value = :value")
})
public class ApiToken implements Externalizable {

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "date_issue", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIssue;

    @NotNull
    @Column(name = "date_expire", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExpire;

    public ApiToken() {
    }

    public ApiToken(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(Date dateIssue) {
        this.dateIssue = dateIssue;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ApiToken other = (ApiToken) obj;
        return Objects.equals(this.user, other.user);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(user);
        out.writeObject(value);
        out.writeObject(dateIssue);
        out.writeObject(dateExpire);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        user = (User) in.readObject();
        value = (String) in.readObject();
        dateIssue = (Date) in.readObject();
        dateExpire = (Date) in.readObject();
    }
}
