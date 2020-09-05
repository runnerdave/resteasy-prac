package net.runnerdave.resteasy.prac.integrationTests;

import net.runnerdave.resteasy.prac.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserIntegrationTestResttemplate {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void itShouldIntegrateAllMethodsInUserEndpoint() throws Exception {

        ParameterizedTypeReference<List<User>> personList = new ParameterizedTypeReference<List<User>>() {
        };

        // GET - ALL USERS
        ResponseEntity<List<User>> response = restTemplate.exchange("/app/api/v1/users", GET, null, personList);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        // POST - NEW INVALID USER
        User bareUser = new User(null, "", "", null, 0, "");
        HttpEntity<User> entity = new HttpEntity<>(bareUser, null);
        ResponseEntity<String> exchange = restTemplate.exchange("/app/api/v1/users", POST, entity, String.class);
        assertThat(exchange.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);

        // GET - ALL USERS
        response = restTemplate.exchange("/app/api/v1/users", GET, null, personList);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        User joe = new User(null, "Joe", "Jones",
                User.Gender.MALE, 22, "joe.jones@gmail.com");
        assertThat(response.getBody().get(0)).isEqualToIgnoringNullFields(joe);

        // GET BY USER BY ID
        UUID uuid = response.getBody().get(0).getUserUid();
        ResponseEntity<User> getUserByIdResponse = restTemplate
                .exchange("/app/api/v1/users/" + uuid, GET, null, User.class);
        assertThat(getUserByIdResponse.getStatusCode()).isEqualTo(OK);
        assertThat(getUserByIdResponse.getBody()).isEqualToIgnoringNullFields(joe);

        // PUT - UPDATE USER BY ID
        User userToUpdate =
                new User(uuid, "John", "Jones", User.Gender.MALE, 22, "john.jones@gmail.com");
        entity = new HttpEntity<>(userToUpdate, null);
        ResponseEntity<User> updateUserByIdResponse = restTemplate
                .exchange("/app/api/v1/users", PUT, entity, User.class);
        assertThat(updateUserByIdResponse.getStatusCode()).isEqualTo(NO_CONTENT);



        // POST - INSERT NEW USER
        User userToInsert = new User(null, "Nelson", "Mandela", User.Gender.MALE, 33, "nelson.mandela@gmail.com");
        entity = new HttpEntity<>(userToInsert, null);
        ResponseEntity<User> insertUserResponse = restTemplate
                .exchange("/app/api/v1/users", POST, entity, User.class, 1);
        assertThat(insertUserResponse.getStatusCode()).isEqualTo(NO_CONTENT);

        // GET - ALL USERS
        response = restTemplate.exchange("/app/api/v1/users", GET, null,
                personList);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().get(1)).isEqualToComparingFieldByField(userToUpdate);
        assertThat(response.getBody().get(0)).isEqualToIgnoringNullFields(userToInsert);

        // DELETE - USER BY ID=1
        ResponseEntity<String> deleteUserResponse = restTemplate
                .exchange("/app/api/v1/users/" + uuid, DELETE, null, String.class);
        assertThat(deleteUserResponse.getStatusCode()).isEqualTo(NO_CONTENT);
        response = restTemplate.exchange("/app/api/v1/users", GET, null,
                personList);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().get(0)).isEqualToIgnoringNullFields(userToInsert);

    }
}
