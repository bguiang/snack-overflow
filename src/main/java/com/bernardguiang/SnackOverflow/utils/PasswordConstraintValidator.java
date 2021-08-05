package com.bernardguiang.SnackOverflow.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

// https://stackabuse.com/spring-custom-password-validation
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
 
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
            // must be 6-20 characters
            new LengthRule(6, 20),

            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // at least one symbol (special character)
            new CharacterRule(EnglishCharacterData.Special, 1),

            // no whitespace
            new WhitespaceRule()

        ));
        
        // Null rule doesn't exist for some reason
        if(password == null) {
    		String nullMessage = "Password is null";
    		context.buildConstraintViolationWithTemplate(nullMessage)
            	.addConstraintViolation();
    		return false;
    	}
        
        RuleResult result = validator.validate(new PasswordData(password));
        
        if (result.isValid()) {
            return true;
        } 
        else {
        	 List<String> messages = validator.getMessages(result);
             
             for(String message : messages) {
             	context.buildConstraintViolationWithTemplate(message)
                 .addConstraintViolation();
             }
             
             return false;
        }
    }
}