package be.vinci.pae.domain.sales;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SaleImpl.class)
public interface Sale extends SaleDTO {

}
