package business.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private long id;
	private String name;
	private String email;
	private String phone;
	private String dni;
    private boolean active;
	private String sagaId;
}