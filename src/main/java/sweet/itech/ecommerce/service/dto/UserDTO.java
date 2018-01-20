package sweet.itech.ecommerce.service.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import sweet.itech.ecommerce.domain.Authority;
import sweet.itech.ecommerce.domain.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

public class UserDTO {
    private Long id;

    @Email
    @NotBlank
    @Size(min=5, max=100)
    private String email;

    @Size(max=50)
    private String firstName;

    @Size(max=50)
    private String lastName;

    private String password;

    private Boolean activated;

    private Instant createdDate;

    private Set<Authority> authorities;

    public UserDTO() {
    }

   public UserDTO(User user){
        this.id = user.getId();
        this.password=user.getPassword();
        this.email = user.getEmail();
        this.firstName= user.getFirstName();
        this.lastName = user.getLastName();
        this.activated = user.getActivated();
        this.createdDate = user.getCreatedDate();
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
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", activated=" + activated +
                ", createdDate=" + createdDate +
                ", authorities=" + authorities +
                '}';
    }
}
