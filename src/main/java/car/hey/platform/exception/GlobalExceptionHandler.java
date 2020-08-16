package car.hey.platform.exception;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonParseException;

import lombok.extern.slf4j.Slf4j;

/**
 * Global Exception Handler
 * 
 * @author Shijin Raj
 * 
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * This function is to handle the InvalidBooking Exception
	 * 
	 * @param invalidBookingException
	 * @return ResponseEntity<ErrorDetails>
	 */
	/*@ExceptionHandler(value = { InvalidBookingException.class })
	public ResponseEntity<ErrorDetails> handleInvalidBookingException(
			final InvalidBookingException invalidBookingException) {
		log.info("Started excecuting handleInvalidBookingException() : ", invalidBookingException.getMessage());

		ErrorDetailsBuilder errorDetailsBuilder = ErrorDetails.builder().code(ErrorCodes.INVALID_REQUESTS.code())
				.type(ErrorCodes.INVALID_REQUESTS.type()).description(ExceptionHandlerConstants.INVALID_PARAMETER);

		errorDetailsBuilder.moreInfo(invalidBookingException.getInformations());
		return new ResponseEntity<ErrorDetails>(errorDetailsBuilder.build(), HttpStatus.BAD_REQUEST);
	}*/

	/**
	 * This function is to handle the IllegalArgument Exception
	 * 
	 * @param illegalArgumentException
	 * @return ResponseEntity<ErrorDetails>
	 */
	@ExceptionHandler(value = { IllegalArgumentException.class })
	public ResponseEntity<ErrorDetails> handleIllegalArgumentException(
			final IllegalArgumentException illegalArgumentException) {
		log.info("Started excecuting handleIllegalArgumentException() : ", illegalArgumentException.getMessage());

		ErrorDetails error = ErrorDetails.builder().code(ErrorCodes.INVALID_REQUESTS.code())
				.type(ErrorCodes.INVALID_REQUESTS.type()).description(ExceptionHandlerConstants.INVALID_PARAMETER)
				.moreInfo(

						Collections.singletonList(
								ErrorInformation.builder().property(ExceptionHandlerConstants.GENERAL_EXCEPTION)
										.description((illegalArgumentException.getMessage() != null
												&& !illegalArgumentException.getMessage().isEmpty())
														? illegalArgumentException.getMessage()
														: illegalArgumentException.getLocalizedMessage())
										.build()))
				.build();

		log.info("General Exception " + illegalArgumentException.getMessage() + " with errorId "
				+ ErrorCodes.INTERNAL_ERROR);

		return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * This function is to handle the binding exceptions
	 * 
	 * @param bindException
	 * @return ResponseEntity<ErrorDetails>
	 */
	/*@ExceptionHandler(value = { BindException.class })
	public ResponseEntity<ErrorDetails> handleBindException(final BindException bindException) {
		log.info("Started excecuting handleBindException() : GlobalExceptionHandler");

		ErrorDetailsBuilder errorDetailsBuilder = ErrorDetails.builder().code(ErrorCodes.INVALID_REQUESTS.code())
				.type(ErrorCodes.INVALID_REQUESTS.type()).description(ExceptionHandlerConstants.INVALID_PARAMETER);

		final List<ErrorInformation> errorInfoList = new ArrayList<ErrorInformation>();
		final List<ObjectError> objError = bindException.getAllErrors();
		for (ObjectError objE : objError) {
			final FieldError fieldError = (FieldError) objE;
			ErrorInformation errorInfo = ErrorInformation.builder().property(fieldError.getField())
					.descriptionEn((objE.getDefaultMessage() != null && !objE.getDefaultMessage().isEmpty())
							? objE.getDefaultMessage()
							: objE.getCode())
					.build();
			errorInfoList.add(errorInfo);
		}
		errorDetailsBuilder.moreInfo(errorInfoList);
		return new ResponseEntity<ErrorDetails>(errorDetailsBuilder.build(), HttpStatus.BAD_REQUEST);
	}*/

	/**
	 * @param nullPointerException
	 * @return ResponseEntity<ErrorDetails>
	 */
	@ExceptionHandler(value = { NullPointerException.class })
	public ResponseEntity<ErrorDetails> handleNullPointerException(final NullPointerException nullPointerException) {
		log.info("Started excecuting handleNullPointerException() : GlobalExceptionHandler");

		ErrorDetails error = ErrorDetails.builder().code(ErrorCodes.INTERNAL_ERROR.code())
				.type(ErrorCodes.INTERNAL_ERROR.type()).description(ExceptionHandlerConstants.CONTACT_SUPPORTTEAM)
				.moreInfo(

						Collections.singletonList(
								ErrorInformation.builder().property(ExceptionHandlerConstants.NULLPOINTER_EXCEPTION)
										.description((nullPointerException.getMessage() != null
												&& !nullPointerException.getMessage().isEmpty())
														? nullPointerException.getMessage()
														: nullPointerException.getLocalizedMessage())
										.build()))
				.build();

		log.info("NullPointerException " + nullPointerException.getMessage() + " with errorId "
				+ ErrorCodes.INTERNAL_ERROR);
		error.setCode(ErrorCodes.INTERNAL_ERROR.code());

		return new ResponseEntity<ErrorDetails>(error, HttpStatus.CONFLICT);
	}

	/**
	 * @param jsonParseException
	 * @return ResponseEntity<ErrorDetails>
	 */
	@ExceptionHandler(value = { JsonParseException.class })
	public ResponseEntity<ErrorDetails> handleJsonParseException(final JsonParseException jsonParseException) {
		log.info("Started excecuting handleJsonParseException() : GlobalExceptionHandler");

		ErrorDetails error = ErrorDetails.builder().code(ErrorCodes.INTERNAL_ERROR.code())
				.type(ErrorCodes.INTERNAL_ERROR.type()).description(ExceptionHandlerConstants.CONTACT_SUPPORTTEAM)
				.moreInfo(

						Collections.singletonList(ErrorInformation.builder()
								.property(ExceptionHandlerConstants.JSONPARSE_EXCEPTION)
								.description((jsonParseException.getMessage() != null
										&& !jsonParseException.getMessage().isEmpty()) ? jsonParseException.getMessage()
												: jsonParseException.getLocalizedMessage())
								.build()))
				.build();

		log.info(
				"JsonParseException " + jsonParseException.getMessage() + " with errorId " + ErrorCodes.INTERNAL_ERROR);

		return new ResponseEntity<ErrorDetails>(error, HttpStatus.CONFLICT);
	}

	/**
	 * @param exception
	 * @return ResponseEntity<ErrorDetails>
	 */
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<ErrorDetails> handleGeneralException(final Exception exception) {

		ErrorDetails error = ErrorDetails.builder().code(ErrorCodes.INTERNAL_ERROR.code())
				.type(ErrorCodes.INTERNAL_ERROR.type()).description(ExceptionHandlerConstants.CONTACT_SUPPORTTEAM)
				.moreInfo(

						Collections.singletonList(ErrorInformation.builder()
								.property(ExceptionHandlerConstants.GENERAL_EXCEPTION)
								.description((exception.getMessage() != null && !exception.getMessage().isEmpty())
										? exception.getMessage()
										: exception.getLocalizedMessage())
								.build()))
				.build();

		log.info("General Exception " + exception.getMessage() + " with errorId " + ErrorCodes.INTERNAL_ERROR);
		return new ResponseEntity<ErrorDetails>(error, HttpStatus.CONFLICT);
	}

}
