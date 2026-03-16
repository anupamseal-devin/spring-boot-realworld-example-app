package io.spring.core.user;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
  private String id;
  private String email;
  private String username;
  private String password;
  private String bio;
  private String image;

  public User(String email, String username, String password, String bio, String image) {
    this.id = UUID.randomUUID().toString();
    this.email = email;
    this.username = username;
    this.password = password;
    this.bio = bio;
    this.image = image;
  }

  public void update(String email, String username, String password, String bio, String image) {
    if (StringUtils.hasText(email)) {
      this.email = email;
    }

    if (StringUtils.hasText(username)) {
      this.username = username;
    }

    if (StringUtils.hasText(password)) {
      this.password = password;
    }

    if (StringUtils.hasText(bio)) {
      this.bio = bio;
    }

    if (StringUtils.hasText(image)) {
      this.image = image;
    }
  }
}
