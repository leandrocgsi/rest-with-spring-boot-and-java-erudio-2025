package br.com.erudio.controllers.docs;

import br.com.erudio.data.dto.request.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface EmailControllerDocs {

    @Operation(
            summary = "Send an Email",
            description = "Sends an email by providing recipient details, subject, and body.",
            tags = {"Email"},
            responses = {
                @ApiResponse(
                    description = "Email sent successfully",
                    responseCode = "200",
                    content = {
                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)),
                        @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = String.class)),
                        @Content(mediaType = MediaType.APPLICATION_YAML_VALUE, schema = @Schema(implementation = String.class))
                    }
                ),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest);

    @Operation(
            summary = "Send an Email with Attachment",
            description = "Sends an email with an attachment by providing recipient details, subject, body, and the attachment file.",
            tags = {"Email"},
            responses = {
                    @ApiResponse(
                            description = "Email with attachment sent successfully",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = String.class)),
                                    @Content(mediaType = MediaType.APPLICATION_YAML_VALUE, schema = @Schema(implementation = String.class))
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequestJson,
            @RequestParam("attachment") MultipartFile attachment);
}