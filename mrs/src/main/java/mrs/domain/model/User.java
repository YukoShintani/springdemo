package mrs.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * ユーザーテーブル(USR)に対応するEntity
 *
 * */
@Entity
@Table(name="USR")
public class User {

	// [USER_ID]カラムに対応するフィールド
	@Id
	@Column(name="USER_ID")
	private String userId;

	// [PASSWORD]カラムに対応するフィールド
	@Column(name="PASSWORD")
	private String password;

	// [FIRST_NAME]カラムに対応するフィールド
	@Column(name="FIRST_NAME")
	private String firstName;

	// [LAST_NAME]カラムに対応するフィールド
	@Column(name="LAST_NAME")
	private String lastName;

	// [ROLE_NAME]カラムに対応するフィールド。Enumの種類の中で設定する
	@Enumerated(EnumType.STRING)
	@Column(name="ROLE_NAME")
	private RoleName roleName;

	// 以下getter/setter
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}

}
