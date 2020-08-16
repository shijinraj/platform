package car.hey.platform.exception;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * This class is to show the error details
 * 
 * @author Shijin Raj
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -518401726540019135L;

	/**
	 * errorCode
	 */
	private long code;

	/**
	 * error_Type
	 */
	private String type;

	/**
	 * error_Description
	 */
	private String description;

	/**
	 * more_Info
	 */
	private List<ErrorInformation> moreInfo;

}
