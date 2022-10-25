package gov.noaa.ncei.geosamples.api.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.geosamples.api.model.ValidBbox.ValidBboxValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidBboxValidator.class)
@Documented
public @interface ValidBbox {

  String message() default "Invalid bounding box";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidBboxValidator implements ConstraintValidator<ValidBbox, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }

      String[] parts = value.split(",");
      if (parts.length != 4) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("invalid number of parameters: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      double xmin;
      double ymin;
      double xmax;
      double ymax;
      try {
        xmin = Double.parseDouble(parts[0].trim());
      } catch (NumberFormatException e) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("xmin is not a number: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      try {
        ymin = Double.parseDouble(parts[1].trim());
      } catch (NumberFormatException e) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("ymin is not a number: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      try {
        xmax = Double.parseDouble(parts[2].trim());
      } catch (NumberFormatException e) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("xmax is not a number: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      try {
        ymax = Double.parseDouble(parts[3].trim());
      } catch (NumberFormatException e) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("ymax is not a number: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      if (xmin < -180D || xmin > 180D) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("xmin must be between -180 and 180: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      if (xmax < -180D || xmax > 180D) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("xmax must be between -180 and 180: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      if (ymin < -180D || ymin > 180D) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("ymin must be between -90 and 90: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      if (ymax < -180D || ymax > 180D) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("ymax must be between -90 and 90: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      if (xmin > xmax) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("xmin can not be greater than xmax: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      if (ymin > ymax) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("ymin can not be greater than ymax: must match xmin,ymin,xmax,ymax")
            .addConstraintViolation();
        return false;
      }
      return true;
    }
  }

}
