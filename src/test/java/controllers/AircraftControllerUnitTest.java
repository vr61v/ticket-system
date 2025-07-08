package controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vr61v.controllers.v1.AircraftController;
import org.vr61v.dtos.aircraft.AircraftRequestDto;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.dtos.aircraft.AircraftResponseLocalizedDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.entities.embedded.LocalizedString;
import org.vr61v.mappers.AircraftMapper;
import org.vr61v.services.impl.AircraftService;
import org.vr61v.types.Locale;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AircraftControllerUnitTest {

    private static final String CODE = "000";
    private static final LocalizedString MODEL = LocalizedString.builder().ru("модель").en("model").build();
    private static final Integer RANGE = 5000;

    private static final Aircraft AIRCRAFT = Aircraft.builder()
            .aircraftCode(CODE)
            .model(MODEL)
            .range(RANGE)
            .build();

    private static final List<Aircraft> AIRCRAFT_LIST = List.of(AIRCRAFT);

    private static final AircraftRequestDto REQUEST = AircraftRequestDto.builder()
            .model(MODEL)
            .range(RANGE)
            .build();

    private static final AircraftResponseDto RESPONSE = AircraftResponseDto.builder()
            .code(CODE)
            .model(MODEL)
            .range(RANGE)
            .build();

    private static final List<AircraftResponseDto> RESPONSE_LIST = List.of(RESPONSE);

    private static final AircraftResponseLocalizedDto RESPONSE_LOCALIZED_RU = AircraftResponseLocalizedDto.builder()
            .code(CODE)
            .model(MODEL.getRu())
            .range(RANGE)
            .build();

    private static final AircraftResponseLocalizedDto RESPONSE_LOCALIZED_EN = AircraftResponseLocalizedDto.builder()
            .code(CODE)
            .model(MODEL.getEn())
            .range(RANGE)
            .build();

    private static final List<AircraftResponseLocalizedDto> RESPONSE_LOCALIZED_RU_LIST = List.of(RESPONSE_LOCALIZED_RU);
    private static final List<AircraftResponseLocalizedDto> RESPONSE_LOCALIZED_EN_LIST = List.of(RESPONSE_LOCALIZED_EN);

    @Test
    @DisplayName("Create aircraft success")
    void create_ShouldCreated(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.create(CODE, REQUEST);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.CREATED)
                .describedAs("Status code should be 201 CREATED");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE)
                .describedAs("Response body should be equals to RESPONSE");
    }

    @Test
    @DisplayName("Update aircraft success")
    void update_ShouldUpdate(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.update(CODE, REQUEST);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK)
                .describedAs("Status code should be 200 SUCCESS");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE)
                .describedAs("Response body should be equals to RESPONSE");
    }

    @Test
    @DisplayName("FindById aircraft success")
    void findById_ShouldFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.findById(CODE, null);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK)
                .describedAs("Status code should be 200 OK");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE)
                .describedAs("Response body should be equals to RESPONSE");
    }

    @Test
    @DisplayName("FindById aircraft not found")
    void findById_ShouldNotFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.findById("NOT_FOUND", null);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND)
                .describedAs("Status code should be 404 NOT FOUND");
    }

    @Test
    @DisplayName("FindById aircraft with ru locale success")
    void findByIdLocaleRu_ShouldFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.findById(CODE, Locale.RU);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK)
                .describedAs("Status code should be 200 OK");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE_LOCALIZED_RU)
                .describedAs("Response body should be equals to RESPONSE_LOCALIZED_RU");
    }

    @Test
    @DisplayName("FindById aircraft with en locale success")
    void findByIdLocaleEn_ShouldFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.findById(CODE, Locale.EN);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK)
                .describedAs("Status code should be 200 OK");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE_LOCALIZED_EN)
                .describedAs("Response body should be equals to RESPONSE_LOCALIZED_EN");
    }

    @Test
    @DisplayName("FindAll aircraft success")
    void findAll_ShouldFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.findAll(null);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK)
                .describedAs("Status code should be 200 OK");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE_LIST)
                .describedAs("Response body should be equals to RESPONSE_LIST");
    }

    @Test
    @DisplayName("FindAll aircraft with ru locale success")
    void findAllLocaleRu_ShouldFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.findAll(Locale.RU);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK)
                .describedAs("Status code should be 200 OK");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE_LOCALIZED_RU_LIST)
                .describedAs("Response body should be equals to RESPONSE_LOCALIZED_RU_LIST");
    }

    @Test
    @DisplayName("FindAll aircraft with en locale success")
    void findAllLocaleEn_ShouldFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.findAll(Locale.EN);
        assertThat(responseEntity)
                .isNotNull()
                .describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK)
                .describedAs("Status code should be 200 OK");
        assertThat(responseEntity.getBody())
                .isNotNull()
                .describedAs("Response body should not be null")
                .isEqualTo(RESPONSE_LOCALIZED_EN_LIST)
                .describedAs("Response body should be equals to RESPONSE_LOCALIZED_EN_LIST");
    }

    @Test
    @DisplayName("Delete aircraft success")
    void delete_ShouldDelete(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.delete(CODE);
        assertThat(responseEntity)
                .isNotNull().describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT)
                .describedAs("Status code should be 204 NO_CONTENT");
    }

    @Test
    @DisplayName("Delete aircraft not found")
    void delete_ShouldNotFound(@Autowired AircraftController controller) {
        ResponseEntity<?> responseEntity = controller.delete("NOT_FOUND");
        assertThat(responseEntity)
                .isNotNull().describedAs("Response should be not null");
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND)
                .describedAs("Status code should be 404 NOT_FOUND");
    }

    @SpringBootConfiguration
    static class TestConfiguration {

        @Bean
        public AircraftService aircraftService() {
            AircraftService service = mock(AircraftService.class);
            when(service.create(any())).thenReturn(AIRCRAFT);
            doAnswer(invocation ->
                    invocation.getArgument(0).equals(CODE) ?
                            Optional.of(AIRCRAFT) :
                            Optional.empty())
                    .when(service)
                    .findById(any());
            when(service.findAll()).thenReturn(AIRCRAFT_LIST);
            when(service.update(any())).thenReturn(AIRCRAFT);
            doNothing().when(service).delete(any());
            return service;
        }

        @Bean
        public AircraftMapper aircraftMapper() {
            AircraftMapper mapper = mock(AircraftMapper.class);
            when(mapper.toDto(AIRCRAFT)).thenReturn(RESPONSE);
            when(mapper.toLocalizedDto(AIRCRAFT, Locale.RU)).thenReturn(RESPONSE_LOCALIZED_RU);
            when(mapper.toLocalizedDto(AIRCRAFT, Locale.EN)).thenReturn(RESPONSE_LOCALIZED_EN);
            when(mapper.toEntity(any())).thenReturn(AIRCRAFT);
            return mapper;
        }

        @Bean
        public AircraftController aircraftController(
                @Autowired AircraftService service, @Autowired  AircraftMapper mapper) {
            return new AircraftController(service, mapper);
        }

    }
}