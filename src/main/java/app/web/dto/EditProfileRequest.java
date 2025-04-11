package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class EditProfileRequest {

    @NotBlank(message = "First name cannot be empty!")
    @Size(min = 5, max = 20, message = "first name length must be between 5 and 20 characters!")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty!")
    @Size(min = 5, max = 20, message = "last name length must be between 5 and 20 characters!")
    private String lastName;

    @NotBlank(message = "Image cannot be empty!")
    @URL
    private String imageUrl;

}



