package mrs.domain.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.domain.model.User;

/*
 * ユーザーEntityに対してリポジトリ操作を提供する
 * */
public interface UserRepository extends JpaRepository<User, String> {

}
