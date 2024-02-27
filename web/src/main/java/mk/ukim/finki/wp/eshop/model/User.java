package mk.ukim.finki.wp.eshop.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.eshop.convertors.UserFullNameConverter;
import mk.ukim.finki.wp.eshop.model.embeddables.UserAddress;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "shop_users")
public class User implements UserDetails {

    @Id
    private String username;

    private String password;

    @Convert(converter = UserFullNameConverter.class)
    private UserFullName fullname;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ShoppingCart> carts;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride( name = "country", column = @Column(name = "user_country")),
        @AttributeOverride( name = "city", column = @Column(name = "user_city")),
        @AttributeOverride( name = "address1", column = @Column(name = "user_address1")),
        @AttributeOverride( name = "address2", column = @Column(name = "user_address2"))
    })
    private UserAddress userAddress;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired =  true;
    private boolean isEnabled = true;

    @Enumerated(value = EnumType.STRING)
    private Role role;


    public User(String username, String password, String name, String surname, UserAddress userAddress, Role role) {
        this.username = username;
        this.password = password;
        this.fullname = new UserFullName(name, surname);
        this.userAddress = userAddress;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
