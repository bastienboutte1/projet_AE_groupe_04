package be.vinci.pae.domain.furnitures;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = FurnitureImpl.class)
public interface Furniture extends FurnitureDTO {

}
