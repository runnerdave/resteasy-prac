package net.runnerdave.resteasy.prac;

import net.runnerdave.resteasy.prac.clientproxy.UserResourceV1;
import net.runnerdave.resteasy.prac.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApplicationTests {

	@Autowired
	private UserResourceV1 userResourceV1;

	@Test
	void itShouldFetchAllUsers() {
		List<User> users = userResourceV1.fetchUsers(null);

		assertThat(users).hasSize(1);

		User joe = new User(null, "Joe", "Jones",
				User.Gender.MALE, 22, "joe.jones@gmail.com");

		assertThat(users.get(0)).isEqualToIgnoringNullFields(joe);
		assertThat(users.get(0).getUserUid()).isInstanceOf(UUID.class);
		assertThat(users.get(0).getUserUid()).isNotNull();
	}

}
