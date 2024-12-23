package br.com.erudio.controllers.docs;

import br.com.erudio.data.dto.PersonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@Tag(name = "People", description = "Endpoints for Managing People")
public interface PersonControllerDocs {

    @Operation(summary = "Finds all People", description = "Finds all People.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                    )
                }
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    List<PersonDTO> findAll();

    @Operation(
        summary = "Finds a person",
        description = "Find a specific person by your ID.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            // Need the empty content otherwise it fills it with the example person schema
            // Setting empty content also hides the box in the swagger ui
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(
        summary = "Adds a new person",
        description = "Adds a new person by passing in a JSON, XML or YML representation of the person.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    PersonDTO create(@RequestBody PersonDTO person);

    @Operation(
        summary = "Updates a person's information",
        description = "Updates a person's information by passing in a JSON, XML or YML representation of the updated person.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Updated",
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    PersonDTO update(@RequestBody PersonDTO person);

    @Operation(
        summary = "Deletes a person",
        description = "Deletes a person by their Id.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "No Content",
                responseCode = "204",
                links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}