package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Metadata required by TDEI and created by the data generator. This data is required to upload data and is provided when data is downloaded.
 */
@Schema(description = "Metadata required by TDEI and created by the data generator. This data is required to upload data and is provided when data is downloaded. ")
@Validated
@Data
public class TdeiGeneratorMetadata {
    @JsonProperty("collection_date")
    @Schema(required = true, description = "date of collection or generation of this data")
    @NotNull
    @Valid
    private LocalDate collectionDate = null;
    @JsonProperty("collection_method")
    @Schema(required = true, description = "how data was collected")
    @NotNull
    private CollectionMethodEnum collectionMethod = null;
    @JsonProperty("collected_by")
    @Schema(required = true, description = "name of entity or agency or person who collected this data")
    @NotNull
    private String collectedBy = null;
    @JsonProperty("valid")
    @Schema(required = true, description = "Indicates if the file is valid or not. Used to invalidate a file.")
    @NotNull
    private ValidEnum valid = null;
    @JsonProperty("valid_from")
    @Schema(required = true, description = "date to which the file is valid")
    @NotNull
    private String validFrom = null;
    @JsonProperty("valid_to")
    @Schema(description = "date to which the file is valid")
    private String validTo = null;


    /**
     * how data was collected
     */
    public enum CollectionMethodEnum {
        MANUAL("manual"),

        ALGORITHMIC("algorithmic");

        private String value;

        CollectionMethodEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CollectionMethodEnum fromValue(String text) {
            for (CollectionMethodEnum b : CollectionMethodEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * Indicates if the file is valid or not. Used to invalidate a file.
     */
    public enum ValidEnum {
        YES("yes"),

        NO("no");

        private String value;

        ValidEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static ValidEnum fromValue(String text) {
            for (ValidEnum b : ValidEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}
