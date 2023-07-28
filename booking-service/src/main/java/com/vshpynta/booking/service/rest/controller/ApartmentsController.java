package com.vshpynta.booking.service.rest.controller;

import com.vshpynta.booking.service.rest.dto.Apartment;
import com.vshpynta.booking.service.rest.dto.GetApartmentsResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Apartments public clients API")
@RestController
@RequestMapping(value = "/public/apartments", produces = APPLICATION_JSON_VALUE)
public class ApartmentsController {

    /**
     * Return apartments.
     * Return empty collection in case when no apartments found.
     *
     * @param apartmentIds - filter active apartments by IDs
     * @return apartments result {@link GetApartmentsResult}.
     */
    @Operation(summary = "Get apartments", responses = {
            @ApiResponse(responseCode = "200", description = "Apartments retrieved successfully")})
    @GetMapping
    public GetApartmentsResult getApartments(
            @RequestParam(value = "apartmentIds", required = false) List<Long> apartmentIds) {
        log.info("Start getting apartments for apartmentsIds={}", apartmentIds);
        var result = GetApartmentsResult.of(List.of(Apartment.of(1L, 335, "Tokio")));
        log.info("Finished getting apartments for apartmentsIds={}. Result: {}",
                apartmentIds, result);
        return result;
    }
}
