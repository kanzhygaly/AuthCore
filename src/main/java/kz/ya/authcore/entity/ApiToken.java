/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author YERLAN
 */
@Entity(name = "api_token")
public class ApiToken implements Externalizable {
    
    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    
    @NotNull
    @Column(name = "email", nullable = false)
    private String value;
    
    @NotNull
    @Column(name = "date_start", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    
    @NotNull
    @Column(name = "date_end", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;

    public ApiToken() {
    }

    public ApiToken(String value, Date dateStart, Date dateEnd) {
        this.value = value;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
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
        out.writeObject(dateStart);
        out.writeObject(dateEnd);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        user = (User) in.readObject();
        value = (String) in.readObject();
        dateStart = (Date) in.readObject();
        dateEnd = (Date) in.readObject();
    }
}
