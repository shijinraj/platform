package car.hey.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

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
@IdClass(VehicleListingId.class)
@Entity
public class VehicleListing {

	@Id
	private Integer dealerId;

	@Id
	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	private String make;

	@Column(nullable = false)
	private String model;

	@Column(nullable = false)
	private Integer kW;

	@Column(nullable = false)
	private Integer year;

	@Column(nullable = false)
	private String color;

	@Column(nullable = false)
	private Integer price;

}
