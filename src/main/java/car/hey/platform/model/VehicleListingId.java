package car.hey.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Vehicle Listing Composite Id - dealerId + code
 * 
 * @author shijin.raj
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
public class VehicleListingId implements Serializable {

	private static final long serialVersionUID = -3510009280981226623L;

	@Id
	private Integer dealerId;

	@Id
	@Column(nullable = false)
	private String code;

}
