package controllers.unit.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonAssertions {

    public static void assertSuccessfulResponse(ResponseEntity<?> response, HttpStatus expectedStatus, Object expectedBody) {
        assertThat(response)
                .isNotNull()
                .extracting(
                        ResponseEntity::getStatusCode,
                        ResponseEntity::getBody
                )
                .containsExactly(expectedStatus, expectedBody);
    }

    public static void assertErrorResponse(ResponseEntity<?> response, HttpStatus expectedStatus) {
        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(expectedStatus);
    }

}
