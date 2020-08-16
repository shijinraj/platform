package car.hey.platform.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * This class is to display the basic information about the error thrown
 * 
 * @author Shijin Raj
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ErrorInformation {

	/**
	 * Property
	 */
	private String property;

	/**
	 * Description
	 */
	private String description;

}
