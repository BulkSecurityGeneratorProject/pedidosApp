package com.adpsoft.pedidosapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.adpsoft.pedidosapp.domain.enumeration.InviteStatus;

/**
 * A Invite.
 */
@Entity
@Table(name = "invite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "guest_mail", nullable = false)
    private String guestMail;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "date")
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InviteStatus status;

    @ManyToOne
    private User host;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestMail() {
        return guestMail;
    }

    public Invite guestMail(String guestMail) {
        this.guestMail = guestMail;
        return this;
    }

    public void setGuestMail(String guestMail) {
        this.guestMail = guestMail;
    }

    public String getGuestName() {
        return guestName;
    }

    public Invite guestName(String guestName) {
        this.guestName = guestName;
        return this;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Invite date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public InviteStatus getStatus() {
        return status;
    }

    public Invite status(InviteStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(InviteStatus status) {
        this.status = status;
    }

    public User getHost() {
        return host;
    }

    public Invite host(User user) {
        this.host = user;
        return this;
    }

    public void setHost(User user) {
        this.host = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invite invite = (Invite) o;
        if(invite.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, invite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Invite{" +
            "id=" + id +
            ", guestMail='" + guestMail + "'" +
            ", guestName='" + guestName + "'" +
            ", date='" + date + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
