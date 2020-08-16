package car.hey.platform.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VehicleListing Entity
 * 
 * @author shijin.raj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {


	@Id
	@NotEmpty(message = "Code is required")
	private String code;

	@NotEmpty(message = "Make is required")
	private String make;

	@NotEmpty(message = "Make is required")
	@Column(nullable = false)
	private String model;

	@NotEmpty(message = "KW is required")
	@Column(nullable = false)
	private Integer kW;

	@NotEmpty(message = "Year is required")
	@Column(nullable = false)
	private Integer year;

	@NotEmpty(message = "Color is required")
	@Column(nullable = false)
	private String color;

	@NotEmpty(message = "Price is required")
	@Column(nullable = false)
	private Integer price;

}
