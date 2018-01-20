package sweet.itech.ecommerce.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="user")
public class User implements Serializable{

    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    @Size(min=5, max=100)
    @Column(name="email", unique = true)
    private String email;

    @Size(max=50)
    @Column(name="first_name", length = 50)
    private String firstName;

    @Size(max=50)
    @Column(name="last_name", length = 50)
    private String lastName;

    @JsonIgnore
    @NotNull
    @Size(min=60, max=60)
    @Column(name="password", length = 60)
    private String password;

    @NotNull
    @Column(name="activated", nullable = false)
    private Boolean activated;

    @Column(name="created_date")
    private Instant createdDate;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name="user_authority",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="authority_name", referencedColumnName = "name")}
    )
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    public User() {
    }

    public User(String email, String firstName, String lastName, String password, Boolean activated, Instant createdDate) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.activated = activated;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", activated=" + activated +
                ", createdDate=" + createdDate +
                '}';
    }
}
