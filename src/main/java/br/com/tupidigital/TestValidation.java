package br.com.tupidigital;

import br.com.tupidigital.dto.RegisterRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

public class TestValidation {
    public static void main(String[] args) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        RegisterRequestDTO dto = new RegisterRequestDTO("Sandro", "sandro@gmail.com", "123456", null, null, null);
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        if (violations.isEmpty()) {
            System.out.println("No validation errors!");
        } else {
            for (ConstraintViolation<RegisterRequestDTO> violation : violations) {
                System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
            }
        }
    }
}
