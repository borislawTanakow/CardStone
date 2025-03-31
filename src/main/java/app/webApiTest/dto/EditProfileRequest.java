package app.webApiTest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class EditProfileRequest {

    @NotNull(message = "Not null first name!")
    @Size(min = 5, max = 20, message = "first name length must be between 5 and 20 characters!")
    private String firstName;

    @NotNull(message = "Not null last name!")
    @Size(min = 5, max = 20, message = "last name length must be between 5 and 20 characters!")
    private String lastName;

    @NotNull(message = "Not null image url!")
    @URL
    private String imageUrl;

}



